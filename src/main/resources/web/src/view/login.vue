<style scoped> @import "./../lib/common.css"; </style>
<style>
  html, body {
    height: 100%;
  }

  body {
    display: -ms-flexbox;
    display: flex;
    -ms-flex-align: center;
    align-items: center;
    text-align: center;
    padding-top: 40px;
    padding-bottom: 40px;
    background-color: #f5f5f5;
  }

  .form-signin {
    width: 100%;
    max-width: 330px;
    padding: 15px;
    margin: auto;
  }

  .form-signin .form-control {
    position: relative;
    box-sizing: border-box;
    height: auto;
    padding: 10px;
    font-size: 16px;
  }

  .form-signin .form-control:focus {
    z-index: 2;
  }

  .form-signin input[type="text"] {
    margin-bottom: -1px;
    border-bottom-right-radius: 0;
    border-bottom-left-radius: 0;
  }

  .form-signin input[type="password"] {
    border-top-left-radius: 0;
    border-top-right-radius: 0;
  }
</style>
<template>
  <form action="/login" class="form-signin" id="form" method="post">
    <h1 class="h3 mb-3 font-weight-normal">SQL Judger</h1>
    <input class="form-control" name="number" placeholder="学号/教工号" required type="text">
    <input class="form-control" name="password" placeholder="密码" required type="password">
    <div class="form-check-inline mt-2">
      <input class="form-check-input" id="typeStudent" name="type" type="radio" value="student">
      <label class="form-check-label" for="typeStudent">
        student
      </label>
    </div>
    <div class="form-check-inline mt-2">
      <input checked class="form-check-input" id="typeTeacher" name="type" type="radio" value="teacher">
      <label class="form-check-label" for="typeTeacher">
        teacher
      </label>
    </div>
    <button class="btn btn-lg btn-primary btn-block mt-4" id="btn" type="submit">登录</button>
  </form>
</template>
<script>
    import $ from 'jquery';
    import * as common from './../lib/commom.js'
    import "bootstrap/dist/js/bootstrap.js"
    import Vue from 'vue'
    import BootstrapVue from 'bootstrap-vue'

    import 'bootstrap/dist/css/bootstrap.css'
    import 'bootstrap-vue/dist/bootstrap-vue.css'
    import 'pc-bootstrap4-datetimepicker/build/css/bootstrap-datetimepicker.css';

    Vue.use(BootstrapVue);

    const app = {
        name: 'app',
    };
    export default app;

    $(document).ready(function () {
        $('#form').submit(function (e) {
            e.preventDefault();
            const from = common.getUrlParams("from");
            const url = from === null ? "/login" : `/login?from=${from}`;
            $.post(url, $(this).serialize(), null, "json")
                .done(function (data) {
                    console.log(data);
                    location.href = data.data;
                })
                .fail(function (jqXHR, textStatus, errorThrown) {
                    console.log(textStatus);
                    console.log(errorThrown);
                    alert("用户名和密码不匹配");
                });
        });
    });
</script>