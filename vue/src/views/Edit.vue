<template>
  <div class="main-1" v-if="type === 1">
    <Edit_1></Edit_1>
  </div>
  <div class="main-2" v-if="type === 2">
    <Edit_2></Edit_2>
  </div>
</template>
<script setup>
import { useWindowStore } from "@/stores";
import { storeToRefs } from "pinia";
import Edit_1 from "@/views-1/Edit.vue";
import Edit_2 from "@/views-2/Edit.vue";

//获取窗口信息
const WindowStore = useWindowStore();
const { type } = storeToRefs(WindowStore);
</script>
<style scoped></style>
