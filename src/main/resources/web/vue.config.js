module.exports = {
    outputDir: '../../../../target/classes/dist',
    configureWebpack: {
        devtool: 'source-map'
    },
    devServer: {
        proxy: {
            '/': {
                target: 'http://localhost:8080',
                changeOrigin: true
            }
        }
    },

    pages: {
        login: {
            entry: 'src/view/login.js',
            template: 'src/template/index.html',
            filename: 'view/login.html',
            title: 'Login',
        },
        admin: {
            entry: 'src/view/admin.js',
            template: 'src/template/index.html',
            filename: 'view/admin.html',
            title: 'Admin',
        },
        teacher: {
            entry: 'src/view/teacher.js',
            template: 'src/template/index.html',
            filename: 'view/teacher.html',
            title: 'Teacher',
        },
        student: {
            entry: 'src/view/student.js',
            template: 'src/template/index.html',
            filename: 'view/student.html',
            title: 'Student',
        },
        exam: {
            entry: 'src/view/student/exam.js',
            template: 'src/template/index.html',
            filename: 'view/student/exam.html',
            title: 'Exam',
        },
        review: {
            entry: 'src/view/teacher/review.js',
            template: 'src/template/index.html',
            filename: 'view/teacher/review.html',
            title: 'Review',
        },
    },

    publicPath: undefined,
    assetsDir: undefined,
    runtimeCompiler: undefined,
    productionSourceMap: false,
    parallel: undefined,
    css: undefined,
    lintOnSave: false
};