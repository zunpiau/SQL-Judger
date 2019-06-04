<template>
  <div>
    <span class="mt-1 font-weight-bold">输出可视化</span>
    <div>
      <label>SQL格式化</label>
      <pre class="code">{{ resultWrapper.formated }}</pre>
    </div>
    <div>
      <div v-if="resultWrapper.dataSet">
        <label>查询结果</label>
        <div v-if="resultWrapper.dataSet.headers.length">
          <table class="table table-striped">
            <thead>
            <tr>
              <th scope="col" v-for="column in resultWrapper.dataSet.headers">{{ column }}</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="row in resultWrapper.dataSet.data">
              <td v-for="item in row">{{ item }}</td>
            </tr>
            </tbody>
          </table>
        </div>
        <div v-else>
          <p>无数据</p>
        </div>
      </div>
      <div v-else-if="resultWrapper.tables">
        <div v-for="table in resultWrapper.tables">
          <label>数据表：{{ table.name }}</label>
          <table class="table table-striped">
            <thead>
            <tr>
              <th scope="col" v-for="column in table.columns">{{ column }}</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="row in table.data">
              <td v-for="item in row">{{ item }}</td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>
      <div v-else-if="resultWrapper.exception">
        <label>发生异常</label>
        <pre class="code">{{ resultWrapper.exception }}</pre>
      </div>
    </div>
    <span class="mt-1 font-weight-bold">原始输出</span>
    <pre class="code">{{ JSON.stringify(resultWrapper) }}</pre>
  </div>
</template>

<script>
    export default {
        name: "ResultWrapper",
        props: {
            resultWrapper: {
                type: Object,
                require: true,
            },
        },
    }
</script>