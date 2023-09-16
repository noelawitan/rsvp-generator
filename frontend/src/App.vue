<template>
  <router-view></router-view>
  <the-loader />
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
label {
  font-weight: bold;
}

.modal-open {
  padding-right: 0 !important;
  overflow: auto !important;
}
</style>