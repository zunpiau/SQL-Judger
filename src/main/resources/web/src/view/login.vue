<style scoped> @import "./../lib/common.css"; </style>
<style>
  html, body {
    height: 100%;
  }

  body {
    background-color: #f5f5f5;
  }

  .form-signin {
    width: 100%;
    max-width: 330px;
    padding-top: 60px;
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
  <div>
    <nav class="navbar navbar-dark fixed-top bg-dark flex-md-nowrap p-0 shadow-sm">
      <span class="navbar-brand col-sm-3 col-md-2 mr-0">SQL在线评测</span>
    </nav>

    <main class="container-fluid" role="main">
      <form action="/login" class="form-signin text-center" id="form" method="post">
        <h1 class="h3 mb-3 font-weight-normal">SQL Judger</h1>
        <input class="form-control" name="number" placeholder="学号/教工号" required type="text">
        <input class="form-control" name="password" placeholder="密码" required type="password">
        <div class="form-check-inline mt-2">
          <input class="form-check-input" id="typeStudent" name="type" type="radio" value="student">
          <label class="form-check-label" for="typeStudent">
            学生
          </label>
        </div>
        <div class="form-check-inline mt-2">
          <input checked class="form-check-input" id="typeTeacher" name="type" type="radio" value="teacher">
          <label class="form-check-label" for="typeTeacher">
            教师
          </label>
        </div>
        <button class="btn btn-lg btn-primary btn-block mt-4" id="btn" type="submit">登录</button>
      </form>
    </main>
  </div>
</template>
<script>
    import $ from 'jquery';
    import * as common from './../lib/commom.js'
    import "bootstrap/dist/js/bootstrap.js"

    import 'bootstrap/dist/css/bootstrap.css'

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