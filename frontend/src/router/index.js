import { createRouter, createWebHistory } from 'vue-router'
import Home from '@/components/Home.vue'
import ProductList from '@/components/ProductList.vue'
import CategoryList from '@/components/CategoryList.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: Home,
    },
    {
      path: '/products',
      name: 'ProductList',
      component: ProductList
    },
    {
      path: '/categories',
      name: 'CategoryList',
      component: CategoryList
    }
  ],
})

export default router
