<template>
  <div class="main-1" v-if="type === 1">
    <Other_1></Other_1>
  </div>
  <div class="main-2" v-if="type === 2"></div>
</template>
<script setup>
import { useWindowStore } from "@/stores";
import { storeToRefs } from "pinia";
import Other_1 from "@/views-1/Other.vue";

//获取窗口信息
const WindowStore = useWindowStore();
const { type } = storeToRefs(WindowStore);
</script>
<style scoped></style>
