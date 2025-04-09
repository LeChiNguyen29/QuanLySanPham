<template>
  <el-dialog v-model="dialogVisible" title="Thêm/Sửa sản phẩm" @close="closeForm">
    <el-form ref="productFormRef" :model="formData" :rules="rules" label-width="120px">
      <el-form-item label="Mã sản phẩm" prop="productCode">
        <el-input v-model="formData.productCode" />
      </el-form-item>

      <el-form-item label="Tên sản phẩm" prop="name">
        <el-input v-model="formData.name" />
      </el-form-item>

      <el-form-item label="Giá" prop="price">
        <el-input v-model.number="formData.price" type="number" min="0" />
      </el-form-item>

      <el-form-item label="Số lượng" prop="quantity">
        <el-input v-model.number="formData.quantity" type="number" min="1" />
      </el-form-item>

      <el-form-item label="Mô tả">
        <el-input v-model="formData.description" type="textarea" />
      </el-form-item>

      <el-form-item label="Trạng thái" prop="status">
        <el-select v-model="formData.status" placeholder="Chọn trạng thái">
          <el-option label="Hoạt động" value="ACTIVE" />
          <el-option label="Ngừng hoạt động" value="INACTIVE" />
        </el-select>
      </el-form-item>

      <el-form-item label="Danh mục" prop="categoryIds">
        <el-select v-model="formData.categoryIds" multiple placeholder="Chọn danh mục">
          <el-option v-for="category in categories" :key="category.id" :label="category.name" :value="category.id" />
        </el-select>
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="closeForm">Hủy</el-button>
      <el-button type="primary" :loading="isSubmitting" @click="saveProduct">Lưu</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, computed, watch, toRaw } from 'vue';
import axios from 'axios';
import { ElMessage } from 'element-plus';

const props = defineProps({ visible: Boolean, productData: Object });
const emits = defineEmits(['update:visible', 'refresh']);

const productFormRef = ref(null);
const categories = ref([]);
const isSubmitting = ref(false);

const dialogVisible = computed({
  get: () => props.visible,
  set: (value) => emits('update:visible', value),
});

const formData = ref({
  id: null,
  productCode: '',
  name: '',
  price: 0,
  quantity: 1,
  description: '',
  status: 'ACTIVE',
  categoryIds: [],
});

const rules = {
  productCode: [{ required: true, message: 'Mã sản phẩm không được để trống', trigger: 'blur' }],
  name: [{ required: true, message: 'Tên sản phẩm không được để trống', trigger: 'blur' }],
  price: [
    { required: true, message: 'Giá không được để trống', trigger: 'blur' },
    { type: 'number', min: 0, message: 'Giá phải là số dương', trigger: 'blur' },
  ],
  quantity: [
    { required: true, message: 'Số lượng không được để trống', trigger: 'blur' },
    { type: 'number', min: 1, message: 'Số lượng phải lớn hơn 0', trigger: 'blur' },
  ],
  categoryIds: [{ required: true, message: 'Vui lòng chọn ít nhất một danh mục', trigger: 'change' }],
};

const fetchCategories = async () => {
  try {
    const response = await axios.get('http://localhost:8080/api/categories');
    categories.value = response.data?.content ?? [];
  } catch (error) {
    ElMessage.error('Lỗi khi tải danh mục');
  }
};

watch(() => props.visible, (newValue) => {
  if (newValue) {
    fetchCategories();
    formData.value = {
      id: props.productData?.id || null,
      productCode: props.productData?.productCode || '',
      name: props.productData?.name || '',
      price: props.productData?.price || 0,
      quantity: props.productData?.quantity || 1,
      description: props.productData?.description || '',
      status: props.productData?.status || 'ACTIVE',
      categoryIds: Array.isArray(props.productData?.categoryIds) ? props.productData?.categoryIds : [],
    };
  }
});

const saveProduct = async () => {
  if (isSubmitting.value) return;
  isSubmitting.value = true;

  productFormRef.value.validate(async (valid) => {
    if (!valid) {
      isSubmitting.value = false;
      return;
    }

    // Kiểm tra nếu giá là 0
    if (formData.value.price === 0) {
      ElMessage.warning('Giá sản phẩm không được bằng 0!');
      isSubmitting.value = false;
      return;
    }

    const plainData = { ...toRaw(formData.value) };
    try {
      if (plainData.id) {
        await axios.put(`http://localhost:8080/api/products/${plainData.id}`, plainData);
        ElMessage.success('Cập nhật sản phẩm thành công!');
      } else {
        await axios.post('http://localhost:8080/api/products/add', plainData);
        ElMessage.success('Thêm sản phẩm thành công!');
      }
      emits('refresh');
      closeForm();
    } catch (error) {
      ElMessage.error(error.response?.data?.message || 'Lỗi khi lưu sản phẩm!');
    } finally {
      isSubmitting.value = false;
    }
  });
};


const closeForm = () => {
  dialogVisible.value = false;
  productFormRef.value?.resetFields();
  formData.value = {
    id: null,
    productCode: '',
    name: '',
    price: 0,
    quantity: 1,
    description: '',
    status: 'ACTIVE',
    categoryIds: [],
  };
  emits('update:visible', false);
};
</script>
