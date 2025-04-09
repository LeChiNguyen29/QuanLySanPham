<template>
  <el-dialog v-model="showDialog" :title="formData.id ? 'Sửa danh mục' : 'Thêm danh mục'" @close="closeDialog">
    <el-form ref="formRef" :model="formData" label-width="120px" :rules="rules">
      <el-form-item label="Tên danh mục" prop="name">
        <el-input v-model="formData.name" placeholder="Nhập tên danh mục" />
      </el-form-item>
      <el-form-item label="Mô tả" prop="description">
        <el-input v-model="formData.description" placeholder="Nhập mô tả" />
      </el-form-item>
      <el-form-item label="Mã danh mục" prop="categoryCode">
        <el-input v-model="formData.categoryCode" placeholder="Nhập mã danh mục" />
      </el-form-item>
      <el-form-item label="Trạng thái" prop="status">
        <el-select v-model="formData.status">
          <el-option label="Hoạt động" value="ACTIVE"></el-option>
          <el-option label="Ngừng hoạt động" value="INACTIVE"></el-option>
        </el-select>
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="closeDialog">Hủy</el-button>
      <el-button type="primary" @click="saveCategory">Lưu</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, watch, defineProps } from 'vue';
import axios from 'axios';
import { ElMessage } from 'element-plus';

const props = defineProps({
  category: Object
});

const emit = defineEmits(['close', 'refresh']);

const showDialog = ref(true);
const formRef = ref(null);
const formData = ref({
  name: '',
  description: '',
  categoryCode: '',
  status: 'ACTIVE'
});

// Luật kiểm tra dữ liệu nhập vào
const rules = ref({
  name: [{ required: true, message: 'Vui lòng nhập tên danh mục', trigger: 'blur' }],
  categoryCode: [{ required: true, message: 'Vui lòng nhập mã danh mục', trigger: 'blur' }]
});

// Theo dõi thay đổi của `category`
watch(() => props.category, (newValue) => {
  formData.value = newValue
    ? { ...newValue }
    : { name: '', description: '', categoryCode: '', status: 'ACTIVE' };
}, { immediate: true });

// Đóng dialog
const closeDialog = () => {
  showDialog.value = false;
  emit('close');
};

// Lưu danh mục và làm mới sản phẩm
const saveCategory = async () => {
  if (!formRef.value) return;

  formRef.value.validate(async (valid) => {
    if (!valid) return;

    try {
      if (formData.value.id) {
        await axios.put(`http://localhost:8080/api/categories/${formData.value.id}`, formData.value);
      } else {
        await axios.post('http://localhost:8080/api/categories', formData.value);
      }
      ElMessage.success('Lưu danh mục thành công!');

      // ⬇⬇⬇ Phát sự kiện để làm mới danh sách sản phẩm ⬇⬇⬇
      emit('refresh'); // Gửi sự kiện để component cha cập nhật danh sách sản phẩm
      closeDialog();
    } catch (error) {
      console.error('Lỗi khi lưu danh mục:', error);
      ElMessage.error('Có lỗi xảy ra, vui lòng thử lại!');
    }
  }); 
};
</script>
