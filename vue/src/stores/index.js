import { defineStore } from "pinia";

//网页窗口信息
export const useWindowStore = defineStore('window', {
    state: () => {
      return {
        //type为1表示电脑端，为2表示手机端
        type: 2,
        width: 0,
        height: 0
      }
    },  
    getters: {
    },
    actions: {
    },
    persist: {
      enabled: true
    }
});