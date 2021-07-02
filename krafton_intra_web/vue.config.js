// cli 3 이상부터 webpack.config.js -> @vue/cli-service 하위에 있음.

module.exports = {
    devServer: {
        port: process.env.VUE_APP_PORT
    }
}