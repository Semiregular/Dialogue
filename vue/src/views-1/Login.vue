<template>
  <div class="login-main">
    <div class="login-title"></div>
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
          >《对话平台用户协议》</span
        >
      </div>
      <div class="button" @click="loginByPassword()">登录</div>
      <dic class="button" @click="step4()">还没有账号？立即注册</dic>
      <div class="other">
        <div class="item" @click="step1()">手机号登录</div>
        <div class="item" @click="step2()">邮箱登录</div>
      </div>
    </div>
    <div class="form" v-if="loginType === 4">
      <div class="title">新用户注册</div>
      <input class="tel" v-model="userName" placeholder="用户名" />
      <input
        class="password"
        type="password"
        v-model="userFirstPassword"
        placeholder="密码"
      />
      <input
        class="password"
        type="password"
        v-model="userSecondPassword"
        placeholder="再次输入密码"
      />
      <div class="notify">
        <input type="checkbox" v-model="ischeck" /> 已阅读和同意<span
          >《对话平台用户协议》</span
        >
      </div>
      <div class="button" @click="insertUserByPassword()">注册</div>
      <div class="other">
        <div class="item" @click="step1()">遇到问题？</div>
        <div class="item" @click="step2()">在线客服</div>
      </div>
    </div>
  </div>
  <div class="pic"></div>
</template>
<script setup>
import { ref, getCurrentInstance } from "vue";
import { ElNotification } from "element-plus";
import { ElMessage } from "element-plus";
import { SHA256 } from "@/utils/SHA256";
import { setCookie } from "@/utils/cookies";
import { getDate } from "@/utils/getDate";
import { useUserStore } from "@/stores/user";

const userStore = useUserStore();
const { proxy } = getCurrentInstance();
let issent = ref(false);
let ischeck = ref(false);
let loginType = ref(1);
let count = ref(60);
let url = "";
let userName = ref();
let userPassword = ref();
let userFirstPassword = ref();
let userSecondPassword = ref();
let userEmail = ref();
let userTel = ref();
let verify = ref();
let trueVerify;

const step1 = () => {
  loginType.value = 1;
};

const step2 = () => {
  loginType.value = 2;
};

const step3 = () => {
  loginType.value = 3;
};

const step4 = () => {
  loginType.value = 4;
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
        .catch((error) => {

        });
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

const loginByPassword = () => {
  if (ischeck.value) {
    if (userName.value) {
      if (userPassword.value) {
        proxy.$axios
          .post("/user/login", {
            loginType: "PASSWORD",
            userName: userName.value,
            userPassword: SHA256(userPassword.value),
          })
          .then((response) => {
            if (response.status === 200) {
              if (response.data.code === 1) {
                setCookie("idxUser", response.data.data.idxUser);
                setCookie("idxPhotoUrl", response.data.data.idxPhotoUrl);
                setCookie("userName", userName.value);
                setCookie("token", response.data.data.token);
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
              if (response.data.code === 20005) {
                ElNotification({
                  title: "登录失败",
                  message: "用户不存在",
                  type: "error",
                  duration: 2000,
                });
              }
            } else {
              ElNotification({
                title: "登录失败",
                message: "登录失败",
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
          message: "请输入密码",
          type: "warning",
          duration: 1000,
          center: true,
          grouping: true,
        });
      }
    } else {
      ElMessage({
        showClose: false,
        message: "请输入用户名",
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

const insertUserByPassword = () => {
  if (ischeck.value) {
    if (userName.value) {
      if (userFirstPassword.value) {
        if (userFirstPassword.value === userSecondPassword.value) {
          proxy.$axios
            .post("/user/insert", {
              loginType: "PASSWORD",
              userName: userName.value,
              userPassword: SHA256(userSecondPassword.value),
              gmtCreate: getDate(),
            })
            .then((response) => {
              if (response.status === 200 && response.data.code === 1) {
                setCookie("idxUser", response.data.data.idxUser);
                setCookie("idxPhotoUrl", response.data.data.idxPhotoUrl);
                setCookie("userName", userName.value);
                setCookie("token", response.data.data.token);
                setCookie("idxPhoto", response.data.data.idxPhoto);
                proxy.$router.push({ name: "home" });
              } else {
                ElNotification({
                  title: "注册失败",
                  message: "注册失败",
                  type: "error",
                  duration: 2000,
                });
              }
            })
            .catch((error) => {
              ElNotification({
                title: "注册失败",
                message: error,
                type: "error",
                duration: 2000,
              });
            });
        } else {
          ElMessage({
            showClose: false,
            message: "密码输入不一致",
            type: "warning",
            duration: 1000,
            center: true,
          });
        }
      } else {
        ElMessage({
          showClose: false,
          message: "请输入密码",
          type: "warning",
          duration: 1000,
          center: true,
        });
      }
    } else {
      ElMessage({
        showClose: false,
        message: "请输入用户名",
        type: "warning",
        duration: 1000,
        center: true,
      });
    }
  } else {
    ElMessage({
      showClose: false,
      message: "请勾选协议",
      type: "warning",
      duration: 1000,
      center: true,
    });
  }
};
</script>
<style scoped>
.pic {
  position: fixed;
  top: 0%;
  left: 0%;
  width: 100%;
  height: 100%;
  opacity: 0.5;
  background-image: url("../assets/pic-1.jpeg");
  background-position: center center;
  background-repeat: no-repeat;
  background-size: 110% 110%;
  z-index: -1;
  transition: all 0.5s ease;
}
.login-main {
  top: 20vh;
  left: 10%;
  width: 80%;
  height: 60vh;
  position: absolute;
  display: flex;
  flex-direction: row;
}
.login-main .login-title {
  width: 40%;
  background-image: url("../assets/title-3.png");
  background-size: contain;
  background-repeat: no-repeat;
  background-position: center center;
  filter: drop-shadow(1px 1px 3px);
}
.login-main .form {
  margin-left: 10%;
  width: 50%;
  display: flex;
  flex-direction: column;
  justify-content: center;
}
.login-main .form .title {
  margin-bottom: 3vh;
  font-size: 3.2vh;
  font-weight: 500;
  user-select: none;
}
.login-main .form input {
  height: 5vh;
  font-size: 2.8vh;
  border-top: 0;
  border-left: 0;
  border-right: 0;
  border-bottom: 0.2vh solid brown;
  outline: none;
  text-indent: 0.5vh;
  background-color: transparent;
}
.login-main .form .form .tel {
  width: 100%;
  margin-top: 6vh;
}
.login-main .form .password {
  width: 100%;
  margin-top: 2vh;
}
.login-main .form .verify {
  display: flex;
  height: 6vh;
  margin-top: 2vh;
}
.login-main .form .verify input {
  width: 60%;
}
.login-main .form .verify .emit {
  width: 35%;
  height: 5vh;
  user-select: none;
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
  cursor: pointer;
}
.login-main .form .button {
  width: 100%;
  height: 5vh;
  margin-top: 2vh;
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
  cursor: pointer;
  user-select: none;
}
.login-main .notify {
  width: 100%;
  height: 5vh;
  margin-top: 2vh;
}
.login-main .notify input {
  height: 1.9vh;
}
.notify span {
  color: rgb(214, 31, 31);
  cursor: pointer;
}
.login-main .other {
  width: 100%;
  margin-top: 2vh;
  height: 5vh;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
}
.login-main .other .item {
  cursor: pointer;
  user-select: none;
}
</style>
