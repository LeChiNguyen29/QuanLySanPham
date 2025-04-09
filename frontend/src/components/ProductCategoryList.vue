<template>
  <div>
    <el-table :data="productCategories" style="width: 100%">
      <el-table-column prop="productName" label="Tên sản phẩm" width="200"></el-table-column>
      <el-table-column prop="categoryName" label="Tên danh mục" width="200"></el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue';
import axios from 'axios';

const productCategories = ref([]);
let intervalId = null;

// Hàm gọi API
const fetchProductCategories = async () => {
  try {
    const response = await axios.get('http://localhost:8080/api/product-categories');

    // Chỉ cập nhật nếu dữ liệu thay đổi
    if (JSON.stringify(response.data) !== JSON.stringify(productCategories.value)) {
      productCategories.value = response.data;
    }
  } catch (error) {
    console.error('Lỗi khi lấy danh sách sản phẩm - danh mục:', error);
  }
};

// Khi component mounted
onMounted(() => {
  fetchProductCategories();
  intervalId = setInterval(fetchProductCategories, 1000); // Kiểm tra mỗi 5 giây
});

// Khi component bị hủy, xóa interval
onUnmounted(() => {
  if (intervalId) clearInterval(intervalId);
});
</script>
