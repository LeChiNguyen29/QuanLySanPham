<template>
  <div>
    <h1>Quản lý Sản phẩm và Danh mục</h1>
    <el-tabs v-model="activeTab">
      <el-tab-pane label="Sản phẩm" name="products">
        <ProductList />
      </el-tab-pane>
      <el-tab-pane label="Danh mục" name="categories">
        <CategoryList />
      </el-tab-pane>
      <el-tab-pane label="Liên kết Sản phẩm - Danh mục" name="product-categories">
        <ProductCategoryList />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>


<script setup>
import { ref } from 'vue';
import ProductList from '@/components/ProductList.vue';
import CategoryList from '@/components/CategoryList.vue';
import ProductCategoryList from '@/components/ProductCategoryList.vue';

const activeTab = ref('products');
</script>
