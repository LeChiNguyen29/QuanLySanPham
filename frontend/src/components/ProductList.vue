<template>
  <div>
    <h1>Quản lý Sản phẩm</h1>
    <el-button type="primary" @click="showAddForm">Thêm sản phẩm</el-button>

    <el-table :data="products" v-loading="loading" style="width: 100%" stripe>
      <el-table-column prop="productCode" label="Mã sản phẩm" width="180"></el-table-column>
      <el-table-column prop="name" label="Tên sản phẩm" width="180"></el-table-column>
      <el-table-column prop="price" label="Giá" width="120"></el-table-column>
      <el-table-column prop="quantity" label="Số lượng" width="100"></el-table-column>
      
      <el-table-column label="Danh mục" width="200">
        <template #default="scope">
          <el-tag v-for="category in scope.row.categories" :key="category.id" type="info" class="category-tag">
            {{ category.name }}
          </el-tag>
        </template>
      </el-table-column>
      
      <el-table-column prop="status" label="Trạng thái" width="150">
        <template #default="scope">
          <el-tag :type="scope.row.status === 'ACTIVE' ? 'success' : 'danger'">
            {{ scope.row.status === 'ACTIVE' ? 'Hoạt động' : 'Ngừng hoạt động' }}
          </el-tag>
        </template>
      </el-table-column>
      
      <el-table-column label="Hành động" width="200">
        <template #default="scope">
          <el-button type="primary" @click="editProduct(scope.row)">Sửa</el-button>
          <el-button type="danger" @click="confirmDelete(scope.row.id)">Xóa</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      v-model:current-page="pagination.page"
      :page-size="pagination.size"
      :total="pagination.total"
      layout="total, prev, pager, next"
      @current-change="fetchProducts"
    />

    <!-- Form sửa/thêm sản phẩm -->
    <ProductForm v-model:visible="showForm" :productData="selectedProduct" @refresh="fetchProducts" />
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue';
import axios from 'axios';
import { ElMessage, ElMessageBox } from 'element-plus';
import ProductForm from '@/components/ProductForm.vue';

const products = ref([]);
const showForm = ref(false);
const selectedProduct = ref({});
const loading = ref(false);

const pagination = ref({
  page: 1,
  size: 5,
  total: 0
});

// 🚀 Hàm lấy danh sách sản phẩm
const fetchProducts = () => {
  loading.value = true;
  axios.get(`http://localhost:8080/api/products?page=${pagination.value.page - 1}&size=${pagination.value.size}`)
    .then((response) => {
      products.value = response.data.content;
      pagination.value.total = response.data.totalElements;
    })
    .catch(() => {
      ElMessage.error('Lỗi khi tải danh sách sản phẩm!');
    })
    .finally(() => {
      loading.value = false;
    });
};

// 🆕 Mở form thêm sản phẩm
const showAddForm = () => {
  selectedProduct.value = {}; 
  showForm.value = true;
};

// 🆕 Mở form sửa sản phẩm
const editProduct = (product) => {
  console.log("🛠 Sửa sản phẩm:", product);
  selectedProduct.value = JSON.parse(JSON.stringify(product)); // Copy tránh lỗi reactive
  selectedProduct.value.categoryIds = product.categories ? product.categories.map(c => c.id) : [];
  showForm.value = true;
};

// 🚀 Kiểm tra dữ liệu trước khi mở form
watch(selectedProduct, (newVal) => {
  console.log("🔍 Dữ liệu sản phẩm khi mở form:", newVal);
});

// 🚮 Xác nhận xóa sản phẩm
const confirmDelete = (id) => {
  ElMessageBox.confirm(
    'Bạn có chắc chắn muốn xóa sản phẩm này không?',
    'Xác nhận',
    {
      confirmButtonText: 'Xóa',
      cancelButtonText: 'Hủy',
      type: 'warning',
    }
  ).then(() => {
    deleteProduct(id);
  }).catch(() => {
    console.log('❌ Hủy xóa sản phẩm');
  });
};

// 🗑 Xóa sản phẩm
const deleteProduct = (id) => {
  axios.delete(`http://localhost:8080/api/products/${id}`)
    .then(() => {
      ElMessage.success('Xóa sản phẩm thành công!');
      fetchProducts();
    })
    .catch(() => {
      ElMessage.error('Lỗi khi xóa sản phẩm!');
    });
};

onMounted(fetchProducts);
</script>

<style scoped>
.category-tag {
  margin: 2px;
}
</style>
