<style scoped> @import "./../lib/sidebar.css"; </style>
<style scoped> @import "./../lib/common.css"; </style>
<template>
  <div class="container-fluid">
    <div class="row">
      <nav class="col-md-2 d-none d-md-block bg-light sidebar">
        <div class="sidebar-sticky">
          <nav class="nav nav-pills flex-column" id="nav_tabs">
            <a class="nav-link active" data-toggle="pill" href="#home" role="tab">
              Dashboard
            </a>
            <a class="nav-link" data-toggle="pill" href="#teacher" role="tab"
               v-on:click.once="loadEntity('/admin/teacher', teachers)">
              教师管理
            </a>
            <a class="nav-link" data-toggle="pill" href="#clazz" role="tab"
               v-on:click.once="loadEntity('/admin/clazz', clazzs)">
              班级管理
            </a>
            <a class="nav-link" data-toggle="pill" href="#teaching" role="tab"
               v-on:click.once="loadEntity('/admin/teaching', teachings)">
              授课管理
            </a>
            <a class="nav-link" data-toggle="pill" href="#student" role="tab"
               v-on:click.once="loadEntity('/admin/student', students)">
              学生管理
            </a>
          </nav>
        </div>
      </nav>

      <main class="col-md-9 ml-sm-auto col-lg-10 px-4 tab-content" role="main">
        <div class="tab-pane fade show active" id="home" role="tabpanel">
          <h2 class="h2 mb-3">Dashboard</h2>
          <h3 class="h3">Section title</h3>
        </div>
        <div class="tab-pane fade show" id="teacher" role="tabpanel">
          <h1 class="h2 mb-3">教师管理</h1>
          <div
              class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pb-2 mb-3 border-bottom">
            <h3 class="h3">教师列表</h3>
            <div class="mb-2 mb-md-0">
              <button class="btn btn-outline-primary" data-target="#teacherModal" data-toggle="modal">添加</button>
            </div>
          </div>
          <div class="table-responsive">
            <table class="table table-striped table-sm">
              <thead>
              <tr>
                <th scope="col">#</th>
                <th scope="col">教工号</th>
                <th scope="col">姓名</th>
                <th scope="col">操作</th>
              </tr>
              </thead>
              <tbody id="teacher_tbody">
              <tr :key="index" v-for="(teacher, index) in teachers">
                <th scope="row">{{ index + 1 }}</th>
                <td>{{ teacher.number }}</td>
                <td>{{ teacher.name }}</td>
                <td>
                  <button class="btn btn-danger btn-sm" v-on:click="deleteTeacher(teacher)">
                    删除
                  </button>
                </td>
              </tr>
              </tbody>
            </table>
          </div>
          <div class="modal fade" id="teacherModal" role="dialog" tabindex="-1">
            <div class="modal-dialog" role="document">
              <div class="modal-content">
                <div class="modal-header">
                  <h5 class="modal-title">添加教师</h5>
                  <button class="close" data-dismiss="modal" type="button">
                    <span>&times;</span>
                  </button>
                </div>
                <div class="modal-body">
                  <form>
                    <input class="form-control mb-2" name="name" placeholder="姓名" v-model="teacher.name">
                    <input class="form-control mb-2" name="password" placeholder="密码" v-model="teacher.password">
                  </form>
                </div>
                <div class="modal-footer">
                  <button class="btn btn-secondary" data-dismiss="modal" type="button">取消</button>
                  <button class="btn btn-primary" type="button" v-on:click="addTeacher">添加</button>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="tab-pane fade" id="clazz" role="tabpanel">
          <h1 class="h2 mb-3">班级管理</h1>
          <div
              class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pb-2 mb-3 border-bottom">
            <h3 class="h3">班级列表</h3>
            <div class="mb-2 mb-md-0">
              <button class="btn btn-outline-primary" data-target="#clazzModal" data-toggle="modal">添加
              </button>
            </div>
          </div>
          <div class="table-responsive">
            <table class="table table-striped table-sm">
              <thead>
              <tr>
                <th scope="col">#</th>
                <th scope="col">年级</th>
                <th scope="col">班级名</th>
                <th scope="col">操作</th>
              </tr>
              </thead>
              <tbody id="clazz_tbody">
              <tr :key="index" v-for="(clazz, index) in clazzs">
                <th scope="row">{{ index + 1 }}</th>
                <td>{{ clazz.grade }}</td>
                <td>{{ clazz.name }}</td>
                <td>
                  <button class="btn btn-danger btn-sm" v-on:click="deleteClazz(clazz)">
                    删除
                  </button>
                </td>
              </tr>
              </tbody>
            </table>
          </div>
          <div class="modal fade" id="clazzModal" role="dialog" tabindex="-1">
            <div class="modal-dialog" role="document">
              <div class="modal-content">
                <div class="modal-header">
                  <h5 class="modal-title">添加班级</h5>
                  <button class="close" data-dismiss="modal" type="button">
                    <span>&times;</span>
                  </button>
                </div>
                <div class="modal-body">
                  <form>
                    <input class="form-control mb-2" id="clazzInputGrade" placeholder="年级"
                           type="number"
                           v-model="clazz.grade">
                    <input class="form-control mb-2" id="clazzInputName" placeholder="班级名"
                           v-model="clazz.name">
                  </form>
                </div>
                <div class="modal-footer">
                  <button class="btn btn-secondary" data-dismiss="modal" type="button">取消</button>
                  <button class="btn btn-primary" type="button" v-on:click="addClazz">添加</button>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="tab-pane fade" id="teaching" role="tabpanel">
          <h1 class="h2 mb-3">授课管理</h1>
          <div
              class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pb-2 mb-3 border-bottom">
            <h3 class="h3">授课列表</h3>
            <div class="mb-2 mb-md-0">
              <button class="btn btn-outline-primary" data-target="#teachingModal" data-toggle="modal">添加
              </button>
            </div>
          </div>
          <div class="table-responsive">
            <table class="table table-striped table-sm">
              <thead>
              <tr>
                <th scope="col">#</th>
                <th scope="col">班级</th>
                <th scope="col">教师</th>
                <th scope="col">操作</th>
              </tr>
              </thead>
              <tbody id="teaching_tbody">
              <tr :key="index" v-for="(teaching, index) in teachings">
                <th scope="row">{{ index + 1 }}</th>
                <td>{{ teaching.clazz.grade }} {{ teaching.clazz.name }}</td>
                <td>{{ teaching.teacher.number }} / {{ teaching.teacher.name }}</td>
                <td>
                  <button class="btn btn-danger btn-sm" v-on:click="deleteTeaching(teaching)">
                    删除
                  </button>
                </td>
              </tr>
              </tbody>
            </table>
          </div>
          <div class="modal fade" id="teachingModal" role="dialog" tabindex="-1">
            <div class="modal-dialog" role="document">
              <div class="modal-content">
                <div class="modal-header">
                  <h5 class="modal-title">添加授课</h5>
                  <button class="close" data-dismiss="modal" type="button">
                    <span>&times;</span>
                  </button>
                </div>
                <div class="modal-body">
                  <form>
                    <select class="form-control mb-2" name="teaching.teacher.number" type="number"
                            v-model="teaching.teacher.number">
                      <option v-bind:value="teacher.number" v-for="teacher in teachers">
                        {{ teacher.number }} {{ teacher.name }}
                      </option>
                    </select>
                    <select class="form-control mb-2" name="teaching.clazz.id" v-model="teaching.clazz.id">
                      <option v-bind:value="clazz.id" v-for="clazz in clazzs">
                        {{ clazz.grade }} {{ clazz.name }}
                      </option>
                    </select>
                  </form>
                </div>
                <div class="modal-footer">
                  <button class="btn btn-secondary" data-dismiss="modal" type="button">取消</button>
                  <button class="btn btn-primary" type="button" v-on:click="addTeaching">添加</button>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="tab-pane fade" id="student" role="tabpanel">
          <h1 class="h2 mb-3">学生管理</h1>
          <div
              class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pb-2 mb-3 border-bottom">
            <h3 class="h3">学生列表</h3>
            <div class="mb-2 mb-md-0">
              <button class="btn btn-outline-primary" data-target="#studentModal" data-toggle="modal">添加
              </button>
            </div>
          </div>
          <div class="table-responsive">
            <table class="table table-striped table-sm">
              <thead>
              <tr>
                <th scope="col">#</th>
                <th scope="col">学号</th>
                <th scope="col">姓名</th>
                <th scope="col">班级</th>
                <th scope="col">操作</th>
              </tr>
              </thead>
              <tbody id="student_tbody">
              <tr :key="index" v-for="(student, index) in students">
                <th scope="row">{{ index + 1 }}</th>
                <td>{{ student.number }}</td>
                <td>{{ student.name }}</td>
                <td>{{ student.clazz.grade }} {{ student.clazz.name }}</td>
                <td>
                  <button class="btn btn-danger btn-sm" v-on:click="deleteStudent(student)">
                    删除
                  </button>
                </td>
              </tr>
              </tbody>
            </table>
          </div>
          <div class="modal fade" id="studentModal" role="dialog" tabindex="-1">
            <div class="modal-dialog" role="document">
              <div class="modal-content">
                <div class="modal-header">
                  <h5 class="modal-title">添加学生</h5>
                  <button class="close" data-dismiss="modal" type="button">
                    <span>&times;</span>
                  </button>
                </div>
                <div class="modal-body">
                  <form>
                    <input class="form-control mb-2" id="studentInputName" placeholder="姓名"
                           v-model="student.name">
                    <input class="form-control mb-2" id="studentInputPassword"
                           placeholder="密码" v-model="student.password">
                    <select class="form-control mb-2" name="student.clazz.id"
                            v-model="student.clazz.id">
                      <option v-bind:value="clazz.id" v-for="clazz in clazzs">
                        {{ clazz.grade }} {{ clazz.name }}
                      </option>
                    </select>
                  </form>
                </div>
                <div class="modal-footer">
                  <button class="btn btn-secondary" data-dismiss="modal" type="button">取消</button>
                  <button class="btn btn-primary" type="button" v-on:click="addStudent">添加</button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </main>
    </div>
  </div>
</template>
<script>
    import $ from 'jquery';
    import "bootstrap/dist/js/bootstrap.js"

    import 'bootstrap/dist/css/bootstrap.css'
    import 'bootstrap-vue/dist/bootstrap-vue.css'
    import * as common from '../lib/commom.js'

    $('#teachingModal').on('show.bs.modal', loadIfNeeded);
    $('#studentModal').on('show.bs.modal', loadIfNeeded);

    function loadIfNeeded() {
        if (mainVue.teachers.length === 0) {
            mainVue.loadTeachers();
        }
        if (mainVue.clazzs.length === 0) {
            mainVue.loadClazzs();
        }
    }

    export default {
        data() {
            return {
                teacher: {},
                teachers: [],
                student: {
                    clazz: {
                        id: 0,
                    }
                },
                students: [],
                clazz: {},
                clazzs: [],
                teaching: {
                    teacher: {
                        number: 0,
                    },
                    clazz: {
                        id: 0,
                    }
                },
                teachings: [],
            }
        },
        methods: {
            loadEntity(url, entities) {
                console.log(entities);
                common.loadEntity(url, entities)
            },
            deleteTeacher(teacher) {
                common.deleteEntity(`/admin/teacher/${teacher.number}`, this.teachers, teacher);
            },
            addTeacher() {
                common.addEntity('/admin/teacher', this.teacher, this.teachers, $('#teacherModal'));
            },
            deleteStudent(student) {
                common.deleteEntity(`/admin/student/${student.number}`, this.students, student);
            },
            addStudent() {
                common.addEntity('/admin/student', this.student, this.students, $('#studentModal'));
            },
            deleteClazz(clazz) {
                common.deleteEntity(`/admin/clazz/${clazz.id}`, this.clazzs, clazz);
            },
            addClazz() {
                common.addEntity('/admin/clazz', this.clazz, this.clazzs, $('#clazzModal'));
            },
            deleteTeaching(teaching) {
                common.deleteEntity(`/admin/teaching/${teaching.id}`, this.teachings, teaching);
            },
            addTeaching() {
                common.addEntity('/admin/teaching', this.teaching, this.teachings, $('#teachingModal'));
            }
        }
    }
</script>