<style scoped> @import "./../lib/sidebar.css"; </style>
<style scoped> @import "./../lib/common.css"; </style>
<template>
  <div>
    <nav class="navbar navbar-dark fixed-top bg-dark flex-md-nowrap p-0 shadow-sm">
      <span class="navbar-brand col-sm-3 col-md-2 mr-0">SQL实验测验系统</span>
      <span class="navbar-text text-light mr-3 font-weight-bold">管理员</span>
    </nav>
    <div class="container-fluid">
      <div class="row">
        <nav class="col-md-2 d-none d-md-block bg-light sidebar">
          <div class="sidebar-sticky">
            <nav class="nav nav-pills flex-column" id="nav_tabs">
              <a class="nav-link active" data-toggle="pill" href="#teacher" role="tab">
                教师管理
              </a>
              <a class="nav-link" data-toggle="pill" href="#clazz" role="tab">
                班级管理
              </a>
              <a class="nav-link" data-toggle="pill" href="#teaching" role="tab"
                 v-on:click.once="common.load('/admin/teaching', teachings)">
                授课管理
              </a>
              <a class="nav-link" data-toggle="pill" href="#student" role="tab"
                 v-on:click.once="common.load('/admin/student', students)">
                学生管理
              </a>
            </nav>
          </div>
        </nav>

        <main class="col-md-9 ml-sm-auto col-lg-10 px-4 tab-content" role="main">
          <div class="tab-pane fade active show" id="teacher" role="tabpanel">
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
                    <button class="btn btn-secondary btn-sm mr-2" data-target="#modifyTeacherModal" data-toggle="modal"
                            v-on:click="showModifyTeacher(teacher, index)">
                      修改
                    </button>
                    <button class="btn btn-danger btn-sm"
                            v-on:click="common.del(`/admin/teacher/${teacher.number}`, teachers, teacher)">
                      删除
                    </button>
                  </td>
                </tr>
                </tbody>
              </table>
            </div>
            <div class="modal fade" id="modifyTeacherModal" ref="modifyTeacherModal" role="dialog" tabindex="-1">
              <div class="modal-dialog" role="document">
                <div class="modal-content">
                  <div class="modal-header">
                    <h5 class="modal-title">修改教师</h5>
                    <button class="close" data-dismiss="modal" type="button">
                      <span>&times;</span>
                    </button>
                  </div>
                  <div class="modal-body">
                    <form>
                      <input class="form-control mb-2" disabled name="number" placeholder="教工号"
                             v-model="selectTeacher.number">
                      <input class="form-control mb-2" name="name" placeholder="姓名" v-model="selectTeacher.name">
                      <input class="form-control mb-2" name="password" placeholder="密码"
                             v-model="selectTeacher.password">
                    </form>
                  </div>
                  <div class="modal-footer">
                    <button class="btn btn-secondary" data-dismiss="modal" type="button">取消</button>
                    <button class="btn btn-primary" type="button" v-on:click="modifyTeacher">修改</button>
                  </div>
                </div>
              </div>
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
                    <button class="btn btn-primary" type="button"
                            v-on:click="common.add('/admin/teacher', teacher, teachers, jquery('#teacherModal'))">
                      添加
                    </button>
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
                    <button class="btn btn-secondary btn-sm mr-2" data-target="#modifyClazzModal" data-toggle="modal"
                            v-on:click="showModifyClazz(clazz, index)">
                      修改
                    </button>
                    <button class="btn btn-danger btn-sm"
                            v-on:click="common.del(`/admin/clazz/${clazz.id}`, clazzs, clazz)">
                      删除
                    </button>
                  </td>
                </tr>
                </tbody>
              </table>
            </div>
            <div class="modal fade" id="modifyClazzModal" ref="modifyClazzModal" role="dialog" tabindex="-1">
              <div class="modal-dialog" role="document">
                <div class="modal-content">
                  <div class="modal-header">
                    <h5 class="modal-title">修改班级</h5>
                    <button class="close" data-dismiss="modal" type="button">
                      <span>&times;</span>
                    </button>
                  </div>
                  <div class="modal-body">
                    <form>
                      <input class="form-control mb-2" placeholder="年级" type="number" v-model="selectClazz.grade">
                      <input class="form-control mb-2" placeholder="班级名" v-model="selectClazz.name">
                    </form>
                  </div>
                  <div class="modal-footer">
                    <button class="btn btn-secondary" data-dismiss="modal" type="button">取消</button>
                    <button class="btn btn-primary" type="button" v-on:click="modifyClazz">修改</button>
                  </div>
                </div>
              </div>
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
                    <button class="btn btn-primary" type="button"
                            v-on:click="common.add('/admin/clazz', clazz, clazzs, jquery('#clazzModal'))">
                      添加
                    </button>
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
                    <button class="btn btn-danger btn-sm"
                            v-on:click="common.del(`/admin/teaching/${teaching.id}`, teachings, teaching)">
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
                    <button class="btn btn-primary" type="button"
                            v-on:click="common.add('/admin/teaching', teaching, teachings, jquery('#teachingModal'))">
                      添加
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="tab-pane fade" id="student" role="tabpanel">
            <h1 class="h2 mb-3">学生管理</h1>
            <div class="d-flex justify-content-between align-items-center pb-2 mb-3 border-bottom">
              <h3 class="h3">学生列表</h3>
              <div class="d-flex justify-content-between align-items-center">
                <div class="dropdown mr-2">
                  <button class="btn btn-outline-secondary dropdown-toggle" data-toggle="dropdown" type="button">
                    班级
                  </button>
                  <div aria-labelledby="dropdownMenuButton" class="dropdown-menu">
                    <div @click="clazzFilter = -1" class="dropdown-item">全部</div>
                    <div @click="clazzFilter = clazz.id" class="dropdown-item" v-for="clazz in clazzs">
                      {{ clazz.grade }} {{ clazz.name }}
                    </div>
                  </div>
                </div>
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
                <tr :key="student.number" v-for="(student, index) in filterStudents">
                  <th scope="row">{{ index + 1 }}</th>
                  <td>{{ student.number }}</td>
                  <td>{{ student.name }}</td>
                  <td>{{ student.clazz.grade }} {{ student.clazz.name }}</td>
                  <td>
                    <button class="btn btn-secondary btn-sm mr-2" data-target="#modifyStudentModal" data-toggle="modal"
                            v-on:click="showModifyStudent(student, index)">
                      修改
                    </button>
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
                      <input class="form-control mb-2" placeholder="姓名" v-model="student.name">
                      <input class="form-control mb-2" placeholder="密码" v-model="student.password">
                      <select class="form-control mb-2" v-model="student.clazz.id">
                        <option v-bind:value="clazz.id" v-for="clazz in clazzs">
                          {{ clazz.grade }} {{ clazz.name }}
                        </option>
                      </select>
                    </form>
                  </div>
                  <div class="modal-footer">
                    <button class="btn btn-secondary" data-dismiss="modal" type="button">取消</button>
                    <button class="btn btn-primary" type="button"
                            v-on:click="common.add('/admin/student', student, students, jquery('#studentModal'))">
                      添加
                    </button>
                  </div>
                </div>
              </div>
            </div>
            <div class="modal fade" id="modifyStudentModal" ref="modifyStudentModal" role="dialog" tabindex="-1">
              <div class="modal-dialog" role="document">
                <div class="modal-content">
                  <div class="modal-header">
                    <h5 class="modal-title">修改学生</h5>
                    <button class="close" data-dismiss="modal" type="button">
                      <span>&times;</span>
                    </button>
                  </div>
                  <div class="modal-body">
                    <form>
                      <input class="form-control mb-2" disabled placeholder="学号" v-model="selectStudent.number">
                      <input class="form-control mb-2" placeholder="姓名" v-model="selectStudent.name">
                      <input class="form-control mb-2" placeholder="密码" v-model="selectStudent.password">
                      <select class="form-control mb-2" v-model="selectStudent.clazz.id">
                        <option v-bind:value="clazz.id" v-for="clazz in clazzs">
                          {{ clazz.grade }} {{ clazz.name }}
                        </option>
                      </select>
                    </form>
                  </div>
                  <div class="modal-footer">
                    <button class="btn btn-secondary" data-dismiss="modal" type="button">取消</button>
                    <button class="btn btn-primary" type="button"
                            v-on:click="modifyStudent">
                      修改
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </main>
      </div>
    </div>
  </div>
</template>
<script>
    import axios from 'axios';
    import Vue from 'vue'
    import $ from 'jquery';
    import "bootstrap/dist/js/bootstrap.js"
    import * as common from '../lib/commom.js'

    import 'bootstrap/dist/css/bootstrap.css'

    export default {
        data() {
            return {
                teacher: {},
                teachers: [],
                selectTeacher: {},
                selectTeacherIndex: null,
                student: {
                    clazz: {
                        id: 0,
                    }
                },
                students: [],
                selectStudent: {
                    clazz: {
                        id: 0,
                    }
                },
                selectStudentIndex: null,
                clazz: {},
                clazzs: [],
                studentFiltered: [],
                selectClazz: {},
                selectClazzIndex: null,
                teaching: {
                    teacher: {
                        number: 0,
                    },
                    clazz: {
                        id: 0,
                    }
                },
                teachings: [],
                common: common,
                jquery: $,
                clazzFilter: -1,
            }
        },
        created() {
            common.load('/admin/teacher', this.teachers);
            common.load('/admin/clazz', this.clazzs);
        },
        computed: {
            filterStudents() {
                return this.students.filter(e => this.clazzFilter === -1 || e.clazz.id === this.clazzFilter)
            }
        },
        methods: {
            showModifyTeacher(teacher, index) {
                this.selectTeacher = teacher;
                this.selectTeacherIndex = index;
            },
            modifyTeacher() {
                axios.put("/admin/teacher", this.selectTeacher)
                    .then(res => {
                        if (res.data.status === 200) {
                            Vue.set(this.teachers, this.selectTeacherIndex, res.data.data);
                            $(this.$refs.modifyTeacherModal).modal("hide")
                        }
                    })
            },
            deleteStudent(student) {
                common.del(`/admin/student/${student.number}`, this.students, student);
            },
            showModifyClazz(clazz, index) {
                this.selectClazz = clazz;
                this.selectClazzIndex = index;
            },
            modifyClazz() {
                axios.put("/admin/clazz", this.selectClazz)
                    .then(res => {
                        if (res.data.status === 200) {
                            Vue.set(this.clazzs, this.selectClazzIndex, res.data.data);
                            $(this.$refs.modifyClazzModal).modal("hide")
                        }
                    })
            },
            showModifyStudent(student, index) {
                this.selectStudent = student;
                this.selectStudentIndex = index;
            },
            modifyStudent() {
                axios.put("/admin/student", this.selectStudent)
                    .then(res => {
                        if (res.data.status === 200) {
                            Vue.set(this.students, this.selectStudentIndex, res.data.data);
                            $(this.$refs.modifyStudentModal).modal("hide")
                        }
                    })
            },
        }
    }
</script>