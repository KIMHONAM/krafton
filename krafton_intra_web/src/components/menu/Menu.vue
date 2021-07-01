<template>
    <div>
    <v-app-bar absolute dense color="#1f202c" dark app>
        <v-app-bar-nav-icon @click.stop="drawer = !drawer"><v-icon>mdi-menu</v-icon></v-app-bar-nav-icon>
        <a href="/" style="text-decoration:none;color:white !important;"><v-app-bar-title>KRAFTON INTRA SYSTEM</v-app-bar-title></a>
        &nbsp;&nbsp;&nbsp;
        <h5>{{subTitle}}</h5>
    </v-app-bar>
    <v-navigation-drawer absolute bottom temporary v-model="drawer">
      <v-list-item>
        <v-list-item-content>
          <v-list-item-title class="text-h6">
            KRAFTON <br>
            INTRA SYSTEM
          </v-list-item-title>
        </v-list-item-content>
      </v-list-item>

      <v-divider></v-divider>

      <v-list
        dense
        nav
      >
        <v-list-item
          v-for="item in items"
          :key="item.title"
          @click="menuActionClick(item.index)"
        >
          <v-list-item-icon>
            <v-icon>{{ item.icon }}</v-icon>
          </v-list-item-icon>

          <v-list-item-content>
            <v-list-item-title>{{ item.title }}</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list>
    </v-navigation-drawer>
    </div>
</template>

<script>
  export default {
    data () {
      return {
        items: [
          { index: 0, title: '휴가 관리', icon: 'mdi-airplane-takeoff', route: '/pto'},
        ],
        right: null,
        drawer: false,
        subTitle: '',
      }
    },
    methods: {
        menuActionClick(idx) {
            this.subTitle = this.items[idx].title
            let router = this.$router;
            if(router.currentRoute.fullPath !== this.items[idx].route){
                router.push(this.items[idx].route)
            }
        }
    }
  }
</script>