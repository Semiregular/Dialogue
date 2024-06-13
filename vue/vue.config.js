const { defineConfig } = require("@vue/cli-service");
const webpack = require('webpack');
const CompressionPlugin = require('compression-webpack-plugin')
module.exports = defineConfig({
  lintOnSave: false,
  transpileDependencies: true,
  configureWebpack: {
    plugins: [
      new webpack.DefinePlugin({
        __VUE_OPTIONS_API__: JSON.stringify(true),
        __VUE_PROD_DEVTOOLS__: JSON.stringify(false),
        __VUE_PROD_HYDRATION_MISMATCH_DETAILS__: JSON.stringify(false)
      }),
      new CompressionPlugin({
        filename: '[path].gz',
        algorithm: 'gzip',
        test: /\.js$|\.css$|\.html$/,
        threshold: 10240,
        minRatio: 0.8,
        deleteOriginalAssets: false
      }),
    ]
  },
  devServer: {
    open: false,
    port: 8088,
    server: true,
    proxy: {
      api: {
        target: "http://localhost:8080/",
        Secure: true,
        ws: true,
        changeOrigin: true,
        pathRewrite: {
          "^/api": "",
        },
      },
    },
  },
});
