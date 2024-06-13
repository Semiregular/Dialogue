<template>
  <div class="main-1" v-if="type === 1">
    <Search_1></Search_1>
  </div>
  <div class="main-2" v-if="type === 2">
    <Search_2></Search_2>
  </div>
</template>
<script setup>
import { useWindowStore } from "@/stores";
import { storeToRefs } from "pinia";
import Search_1 from "@/views-1/Search.vue";
import Search_2 from "@/views-2/Search.vue";

//获取窗口信息
const WindowStore = useWindowStore();
const { type } = storeToRefs(WindowStore);
</script>
<style scoped></style>
