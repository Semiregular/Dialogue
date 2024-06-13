import { createRouter, createWebHistory } from "vue-router";
import { getCookie } from "@/utils/cookies";


const router = new createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: "/",
      name: "home",
      component: ()=> import("@/views/Home.vue"),
      meta: {
        title: "对话 | 首页",
      },
    },
    {
      path: "/login",
      name: "login",
      component: ()=> import("@/views/Login.vue"),
      meta: {
        title: "对话 | 登录",
      },
    },
    {
      path: "/user",
      name: "user",
      component: ()=> import("@/views/User.vue"),
      meta: {
        title: "对话 | 用户主页",
      },
    },
    {
      path: "/other",
      name: "other",
      component: ()=> import("@/views/Other.vue"),
      meta: {
        title: "对话 | 用户主页",
      },
    },
    {
      path: "/message",
      name: "message",
      component: ()=> import("@/views/Message.vue"),
      meta: {
        title: "对话 | 消息列表",
      },
    },
    {
      path: "/search",
      name: "search",
      component: ()=> import("@/views/Search.vue"),
      meta: {
        title: "对话 | 搜索",
      },
    },
    {
      path: "/read",
      name: "read",
      component: ()=> import("@/views/Read.vue"),
      meta: {
        title: "对话 | 阅读",
      },
    },
    {
      path: "/edit",
      name: "edit",
      component: ()=> import("@/views/Edit.vue"),
      meta: {
        title: "对话 | 文章编辑",
      },
    },
    {
      path: "/update",
      name: "update",
      component: ()=> import("@/views/Update.vue"),
      meta: {
        title: "对话 | 文章编辑",
      },
    },
    {
      path: "/activity",
      name: "activity",
      component: ()=> import("@/views/Activity.vue"),
      meta: {
        title: "对话 | 话题活动",
      },
    },
  ],
});

router.beforeEach((to, from, next) => {
  if (to.meta.isAuth) {
    if (getCookie("token").token) {
      next();
    } else {
      alert("抱歉，您尚未登录！");
      next("/login");
    }
  } else {
    next();
  }
});

router.afterEach((to, from) => {
  document.title = to.meta.title || "对话 | 欢迎";
});

export default router;
