<template>
  <div class="mb-5">
    <router-view></router-view>
  </div>
  <the-loader/>
  <the-footer/>
</template>

<script>
import TheLoader from "@/components/TheLoader.vue";
import TheFooter from '@/components/TheFooter.vue';

export default {
  name: 'App',
  components: {
    TheLoader,
    TheFooter
  }
}
</script>

<style>
body {
  background-color: #e6e0ec !important;
}

label {
  font-weight: bold;
}

.bg-purple {
  background-color: #3c1e70 !important;
  color: #fff !important;
}

.modal-open {
  padding-right: 0 !important;
  overflow: auto !important;
}

.modal-header {
  background-color: #3c1e70 !important;
  color: #fff !important;
}

.font-weight-bold {
  font-weight: bold;
}

</style>