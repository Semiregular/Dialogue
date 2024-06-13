<template>
    <div class="main-1" v-if="type===1">
       <Login_1></Login_1>
    </div>
    <div class="main-2" v-if="type===2">
      <Login_2></Login_2>
    </div>
</template>
<script setup>
import { useWindowStore } from '@/stores';
import { storeToRefs } from "pinia";
import Login_1 from '@/views-1/Login.vue'
import Login_2 from '@/views-2/Login.vue'
  
  
  //获取窗口信息
  const WindowStore = useWindowStore();
  const { type } = storeToRefs(WindowStore);
  
  
  </script>
  <style scoped>
  
  </style>