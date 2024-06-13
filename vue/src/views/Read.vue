<template>
    <div class="main-1" v-if="type === 1">
      <Read_1></Read_1>
    </div>
    <div class="main-2" v-if="type === 2">
      <Read_2></Read_2>
    </div>
  </template>
  <script setup>
  import { useWindowStore } from "@/stores";
  import { storeToRefs } from "pinia";
  import Read_1 from "@/views-1/Read.vue";
  import Read_2 from "@/views-2/Read.vue";
  
  //获取窗口信息
  const WindowStore = useWindowStore();
  const { type } = storeToRefs(WindowStore);
  </script>
  <style scoped></style>