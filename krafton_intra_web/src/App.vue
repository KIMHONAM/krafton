<template>
  <v-app>

    <Menu></Menu>

    <v-main>
      <v-container fluid>
        <router-view></router-view>
      </v-container>
    </v-main>

    <v-footer app>
      <!-- -->
    </v-footer>
  </v-app>
</template>
<style>
@import url('https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100;300;400;500;700;900&display=swap');
.body,
.v-application{
    font-family: 'Noto Sans Kr', sans-serif !important;
}
</style>
<script>
import Menu from '@/components/menu/Menu'

export default {
  name: 'App',
  components: {Menu},
  data: () => ({
      }),
      mounted () {
        console.log('11111111');
        console.log(process.env);
      },
      methods: {
      },
      computed: {
      },

};
</script>