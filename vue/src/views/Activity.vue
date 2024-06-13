<template>
  <div class="activity-2" v-if="type === 2">
    <Activity_2></Activity_2>
  </div>
  <div class="activity-1" v-if="type === 1">
    <Activity_1></Activity_1>
  </div>
</template>
<script setup>
import { useWindowStore } from "@/stores";
import { storeToRefs } from "pinia";
import Activity_1 from "@/views-1/Activity.vue";
import Activity_2 from "@/views-2/Activity.vue";

//获取窗口信息
const WindowStore = useWindowStore();
const { type } = storeToRefs(WindowStore);
</script>
<style scoped></style>
