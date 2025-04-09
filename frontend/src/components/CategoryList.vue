<template>
  <div>
    <h1>Quản lý Danh Mục</h1>
    <el-button type="primary" @click="openForm(null)">Thêm danh mục</el-button>

    <!-- Bảng danh mục -->
    <el-table :data="categories" style="width: 100%" border>
      <el-table-column prop="id" label="ID" width="80" align="center"></el-table-column>
      <el-table-column prop="name" label="Tên danh mục" width="200"></el-table-column>
      <el-table-column prop="description" label="Mô tả"></el-table-column>
      <el-table-column prop="categoryCode" label="Mã danh mục" width="150" align="center"></el-table-column>
      <el-table-column prop="status" label="Trạng thái" width="150" align="center">
        <template #default="scope">
          <el-tag :type="scope.row.status === 'ACTIVE' ? 'success' : 'danger'">
            {{ scope.row.status === 'ACTIVE' ? 'Hoạt động' : 'Ngừng hoạt động' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="Hành động" width="200" align="center">
        <template #default="scope">
          <el-button type="primary" @click="openForm(scope.row)">Sửa</el-button>
          <el-button type="danger" @click="confirmDelete(scope.row.id)">Xóa</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- Phân trang -->
    <el-pagination
      v-model:current-page="pagination.page"
      :page-size="pagination.size"
      :total="pagination.total"
      layout="total, prev, pager, next"
      @current-change="fetchCategories"
    />

    <!-- Form Thêm/Sửa danh mục -->
    <CategoryForm v-if="showForm" :category="selectedCategory" @close="showForm = false" @refresh="fetchCategories" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { ElMessage, ElMessageBox } from 'element-plus';
import CategoryForm from './CategoryForm.vue';

const categories = ref([]);
const showForm = ref(false);
const selectedCategory = ref(null);

const pagination = ref({
  page: 1,
  size: 2,  // Số danh mục trên mỗi trang
  total: 0
});

// Lấy danh sách danh mục với phân trang
const fetchCategories = async () => {
  try {
    const response = await axios.get(`http://localhost:8080/api/categories?page=${pagination.value.page - 1}&size=${pagination.value.size}`);
    categories.value = response.data.content;
    pagination.value.total = response.data.totalElements;
  } catch (error) {
    console.error('Lỗi khi lấy danh mục:', error);
  }
};

// Mở form thêm/sửa
const openForm = (category) => {
  selectedCategory.value = category ? { ...category } : { name: '', description: '', categoryCode: '', status: 'ACTIVE' };
  showForm.value = true;
};

// Xóa danh mục có xác nhận
const confirmDelete = async (id) => {
  try {
    await ElMessageBox.confirm('Bạn có chắc muốn xóa danh mục này?', 'Xác nhận', {
      confirmButtonText: 'Xóa',
      cancelButtonText: 'Hủy',
      type: 'warning'
    });

    await axios.delete(`http://localhost:8080/api/categories/${id}`);
    ElMessage.success('Xóa danh mục thành công!');
    fetchCategories();
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('Lỗi khi xóa danh mục!');
      console.error(error);
    }
  }
};

onMounted(fetchCategories);
</script>

<style scoped>
.el-table th, .el-table td {
  text-align: center;
}
</style>
