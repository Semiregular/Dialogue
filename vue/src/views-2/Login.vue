<template>
  <div :class="{ main: true, 'main-after': isstep0 }">
    <div class="title"></div>
    <div class="button" @click="step0()">
      <div class="item">登录</div>
    </div>
  </div>
  <div :class="{ wrapper: true, 'wrapper-active': isstep0 }">
    <div class="nav">
      <div class="icon" @click="step0()">
        <el-icon><Back /></el-icon>
      </div>
    </div>
    <div class="form" v-if="loginType === 1">
      <div class="title">手机验证码登录</div>
      <input class="tel" v-model="userTel" placeholder="手机号" />
      <div class="verify">
        <input v-model="verify" placeholder="验证码" />
        <div class="emit" v-if="!issent" @click="sent()">发送验证码</div>
        <div class="emit" v-if="issent">{{ count }}s</div>
      </div>
      <div class="notify">
        <input type="checkbox" v-model="ischeck" /> 已阅读和同意<span
          style="color: rgb(214, 31, 31)"
          >《对话平台用户协议》</span
        >
      </div>
      <div class="button" @click="loginByTel()">登录</div>
      <div class="other">
        <div class="item" @click="step3()">密码登录</div>
        <div class="item" @click="step2()">邮箱登录</div>
      </div>
    </div>
    <div class="form" v-if="loginType === 2">
      <div class="title">邮箱验证码登录</div>
      <input class="tel" v-model="userEmail" placeholder="邮箱" />
      <div class="verify">
        <input v-model="verify" placeholder="验证码" />
        <div class="emit" v-if="!issent" @click="sent()">发送验证码</div>
        <div class="emit" v-if="issent">{{ count }}s</div>
      </div>
      <div class="notify">
        <input type="checkbox" v-model="ischeck" /> 已阅读和同意<span
          style="color: rgb(214, 31, 31)"
          >《对话平台用户协议》</span
        >
      </div>
      <div class="button" @click="loginByEmail()">登录</div>
      <div class="other">
        <div class="item" @click="step1()">手机号登录</div>
        <div class="item" @click="step3()">密码登录</div>
      </div>
    </div>
    <div class="form" v-if="loginType === 3">
      <div class="title">用户名密码登录</div>
      <input class="tel" v-model="userName" placeholder="用户名" />
      <input
        class="password"
        type="password"
        v-model="userPassword"
        placeholder="密码"
      />
      <div class="notify">
        <input type="checkbox" v-model="ischeck" /> 已阅读和同意<span
          style="color: rgb(214, 31, 31)"
          >《对话平台用户协议》</span
        >
      </div>
      <div class="button" @click="loginByPassword()">登录</div>
      <div class="other">
        <div class="item" @click="step1()">手机号登录</div>
        <div class="item" @click="step2()">邮箱登录</div>
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref, getCurrentInstance } from "vue";
import { ElNotification } from "element-plus";
import { ElMessage } from "element-plus";
import { SHA256 } from "@/utils/SHA256";
import { setCookie } from "@/utils/cookies";
import { useUserStore } from "@/stores/user";

const userStore = useUserStore();
const { proxy } = getCurrentInstance();
let isstep0 = ref(false);
let issent = ref(false);
let ischeck = ref(false);
let loginType = ref(1);
let count = ref(60);
let url = "";
let userName = ref();
let userPassword = ref();
let userEmail = ref();
let userTel = ref();
let verify = ref();

const step0 = () => {
  isstep0.value = !isstep0.value;
};

const step1 = () => {
  loginType.value = 1;
};

const step2 = () => {
  loginType.value = 2;
};

const step3 = () => {
  loginType.value = 3;
};

const sent = () => {
  if (loginType.value === 2) {
    if (userEmail.value) {
      issent.value = true;
      let timer = setInterval(() => {
        if (count.value <= 0) {
          clearInterval(timer);
          issent.value = false;
          count.value = 60;
        } else {
          count.value = count.value - 1;
        }
      }, 1000);
      url = `/mail/send?receiver=${userEmail.value}`;
      proxy.$axios
        .get(url)
        .then((response) => {
          if (response.status === 200 && response.data.code === 1) {
            trueVerify = response.data.data.code;
          }
        })
        .catch((error) => {});
    } else {
      ElMessage({
        showClose: false,
        message: "请填写邮箱",
        type: "warning",
        duration: 1000,
        center: true,
        grouping: false,
      });
    }
  }
  if (loginType.value === 1) {
    if (userTel.value) {
      issent.value = true;
      let timer = setInterval(() => {
        if (count.value <= 0) {
          clearInterval(timer);
          issent.value = false;
          count.value = 60;
        } else {
          count.value = count.value - 1;
        }
      }, 1000);
      url = `/user/tel/send?tel=${userTel.value}`;
      proxy.$axios
        .get(url)
        .then((response) => {
          if (response.status === 200 && response.data.code === 1) {
            trueVerify = response.data.data;
          }
        })
        .catch((error) => {
          ElNotification({
            title: "失败",
            message: error,
            type: "error",
          });
        });
    } else {
      ElMessage({
        showClose: false,
        message: "请填写手机号",
        type: "warning",
        duration: 1000,
        center: true,
        grouping: false,
      });
    }
  }
};

const loginByPassword = () => {
  if (ischeck.value) {
    proxy.$axios
      .post("/user/login", {
        loginType: "PASSWORD",
        userName: userName.value,
        userPassword: SHA256(userPassword.value),
      })
      .then((response) => {
        if (response.status === 200 && response.data.code === 1) {
          setCookie("idxUser", response.data.data.idxUser);
          setCookie("userName", response.data.data.userName);
          setCookie("token", response.data.data.token);
          setCookie("idxPhotoUrl", response.data.data.idxPhotoUrl);
          setCookie("idxPhoto", response.data.data.idxPhoto);
          userStore.$patch({
            messageUncheck: response.data.data.messageUncheck,
          });
          proxy.$router.push({ name: "home" });
        }
      })
      .catch((error) => {
        ElNotification({
          title: "登录失败",
          message: error,
          type: "error",
          duration: 2000,
        });
      });
  } else {
    ElMessage({
      showClose: false,
      message: "请勾选协议",
      type: "warning",
      duration: 1000,
      center: true,
      grouping: true,
    });
  }
};

const loginByEmail = () => {
  if (ischeck.value) {
    if (userEmail.value) {
      if (issent.value) {
        if (verify.value) {
          if (verify.value === trueVerify) {
            proxy.$axios
              .post("/user/login", {
                loginType: "EMAIL",
                userEmail: userEmail.value,
                gmtCreate: getDate(),
              })
              .then((response) => {
                if (response.status === 200) {
                  if (response.data.code === 1) {
                    setCookie("idxUser", response.data.data.idxUser);
                    setCookie("userName", response.data.data.userName);
                    setCookie("token", response.data.data.token);
                    setCookie("idxPhotoUrl", response.data.data.idxPhotoUrl);
                    setCookie("idxPhoto", response.data.data.idxPhoto);
                    userStore.$patch({
                      messageUncheck: response.data.data.messageUncheck,
                    });
                    proxy.$router.push({ name: "home" });
                  }
                  if (response.data.code === 20002) {
                    ElNotification({
                      title: "登录失败",
                      message: "用户已登录",
                      type: "error",
                      duration: 2000,
                    });
                  }
                } else {
                  ElNotification({
                    title: "登录失败",
                    message: "失败",
                    type: "error",
                    duration: 2000,
                  });
                }
              })
              .catch((error) => {
                ElNotification({
                  title: "登录失败",
                  message: error,
                  type: "error",
                  duration: 2000,
                });
              });
          } else {
            ElMessage({
              showClose: false,
              message: "验证码有误",
              type: "warning",
              duration: 1000,
              center: true,
              grouping: true,
            });
          }
        } else {
          ElMessage({
            showClose: false,
            message: "请填写验证码",
            type: "warning",
            duration: 1000,
            center: true,
            grouping: true,
          });
        }
      } else {
        ElMessage({
          showClose: false,
          message: "验证码已失效",
          type: "warning",
          duration: 1000,
          center: true,
          grouping: true,
        });
      }
    } else {
      ElMessage({
        showClose: false,
        message: "请填写邮箱",
        type: "warning",
        duration: 1000,
        center: true,
        grouping: true,
      });
    }
  } else {
    ElMessage({
      showClose: false,
      message: "请勾选协议",
      type: "warning",
      duration: 1000,
      center: true,
      grouping: true,
    });
  }
};
const loginByTel = () => {
  if (ischeck.value) {
    if (userTel.value) {
      if (issent.value) {
        if (verify.value) {
          if (verify.value === trueVerify) {
            proxy.$axios
              .post("/user/login", {
                loginType: "TEL",
                userTel: userTel.value,
                gmtCreate: getDate(),
              })
              .then((response) => {
                if (response.status === 200) {
                  if (response.data.code === 1) {
                    setCookie("idxUser", response.data.data.idxUser);
                    setCookie("userName", response.data.data.userName);
                    setCookie("token", response.data.data.token);
                    setCookie("idxPhotoUrl", response.data.data.idxPhotoUrl);
                    setCookie("idxPhoto", response.data.data.idxPhoto);
                    userStore.$patch({
                      messageUncheck: response.data.data.messageUncheck,
                    });
                    proxy.$router.push({ name: "home" });
                  }
                  if (response.data.code === 20002) {
                    ElNotification({
                      title: "登录失败",
                      message: "用户已登录",
                      type: "error",
                      duration: 2000,
                    });
                  }
                } else {
                  ElNotification({
                    title: "登录失败",
                    message: "失败",
                    type: "error",
                    duration: 2000,
                  });
                }
              })
              .catch((error) => {
                ElNotification({
                  title: "登录失败",
                  message: error,
                  type: "error",
                  duration: 2000,
                });
              });
          } else {
            ElMessage({
              showClose: false,
              message: "验证码有误",
              type: "warning",
              duration: 1000,
              center: true,
              grouping: true,
            });
          }
        } else {
          ElMessage({
            showClose: false,
            message: "请填写验证码",
            type: "warning",
            duration: 1000,
            center: true,
            grouping: true,
          });
        }
      } else {
        ElMessage({
          showClose: false,
          message: "验证码已失效",
          type: "warning",
          duration: 1000,
          center: true,
          grouping: true,
        });
      }
    } else {
      ElMessage({
        showClose: false,
        message: "请填写手机号",
        type: "warning",
        duration: 1000,
        center: true,
        grouping: true,
      });
    }
  } else {
    ElMessage({
      showClose: false,
      message: "请勾选协议",
      type: "warning",
      duration: 1000,
      center: true,
      grouping: true,
    });
  }
};
</script>
<style scoped>
.main {
  width: 100%;
  height: 100vh;
  display: flex;
  flex-direction: column;
  z-index: 3;
  transition: all 0.5s ease;
}
.main-after {
  opacity: 0;
  margin-left: -50%;
  transition: all 0.5s ease;
}
.main .title {
  width: 60%;
  height: 30%;
  margin-top: 10vh;
  margin-left: 20%;
  background-image: url("../assets/title-1.png");
  background-size: contain;
  background-repeat: no-repeat;
  background-position: center center;
}
.main .button {
  margin-top: 30vh;
  margin-left: 20%;
  width: 60%;
  height: 6vh;
  display: flex;
  flex-direction: column;
  justify-content: center;
}
.main .button .item {
  width: 100%;
  height: 6vh;
  color: white;
  margin-bottom: 1vh;
  display: flex;
  flex-direction: column;
  justify-content: center;
  font-size: 18px;
  font-weight: 500;
  text-align: center;
  border-radius: 1vh;
  z-index: 5;
  background-color: rgb(214, 31, 31);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
}
.wrapper {
  top: 0%;
  left: 50%;
  width: 90%;
  height: 100vh;
  position: fixed;
  opacity: 0;
  z-index: 2;
  display: flex;
  flex-direction: column;
  transition: all 0.2s ease;
}
.wrapper-active {
  left: 5%;
  opacity: 1;
  transition: all 0.5s ease;
}
.wrapper .nav {
  width: 100%;
  height: 6vh;
  padding-top: 2vh;
}
.wrapper .nav .icon {
  font-size: 4vh;
  font-weight: 500;
  color: rgb(214, 31, 31);
}
.wrapper .title {
  margin-top: 2vh;
  font-size: 3.2vh;
  font-weight: 500;
}
.wrapper input {
  height: 5vh;
  font-size: 2.8vh;
  border-top: 0;
  border-left: 0;
  border-right: 0;
  border-bottom: 0.2vh solid brown;
  outline: none;
  text-indent: 0.5vh;
}
.wrapper .form .tel {
  width: 100%;
  margin-top: 6vh;
}
.wrapper .form .password {
  width: 100%;
  margin-top: 2vh;
}
.wrapper .form .verify {
  display: flex;
  height: 6vh;
  margin-top: 2vh;
}
.wrapper .form .verify input {
  width: 60%;
}
.wrapper .form .verify .emit {
  width: 35%;
  height: 5vh;
  margin-left: 5%;
  font-size: 2.5vh;
  text-align: center;
  color: white;
  border-radius: 1vh;
  display: flex;
  flex-direction: column;
  justify-content: center;
  background-color: rgb(214, 31, 31);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
}
.wrapper .form .button {
  width: 100%;
  height: 5vh;
  margin-top: 1vh;
  font-weight: 500;
  font-size: 2.5vh;
  border-radius: 1vh;
  background-color: rgb(214, 31, 31);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
  color: white;
  text-align: center;
  display: flex;
  flex-direction: column;
  justify-content: center;
}
.wrapper .notify {
  width: 100%;
  height: 5vh;
  margin-top: 2vh;
}
.wrapper .notify input {
  height: 1.9vh;
}
.wrapper .other {
  width: 100%;
  margin-top: 2vh;
  height: 5vh;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
}
</style>
