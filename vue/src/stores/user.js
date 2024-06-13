import { defineStore } from "pinia";

export const useUserStore = defineStore("user", {
  state: () => {
    return {
      messageUncheck: 0,
      ip: ["未知", "未知"],
    };
  },
  getters: {},
  actions: {},
  persist: {
    enabled: true,
  },
});
