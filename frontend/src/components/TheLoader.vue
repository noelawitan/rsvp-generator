<template>
  <div v-if="isVisible" class="loader-background">
    <div class="loader"></div>
  </div>
</template>

<script>
export default {
  computed: {
    isVisible() {
      return this.$loader.isVisible;
    }
  }
}
</script>


<style scoped>
.loader-background {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.7);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
}

.loader {
  width: 7em;
  height: 5em;
  position: relative;
  animation: beat 1s infinite;
}

.loader::before,
.loader::after {
  content: "";
  position: absolute;
  top: 0;
  width: 50px;
  height: 80px;
  border-radius: 5em 5em 0 0;
  animation: coldblue 1s infinite;
}

.loader::before {
  left: 50px;
  transform: rotate(-45deg);
  transform-origin: 0 100%;
}

.loader::after {
  left: 0;
  transform: rotate(45deg);
  transform-origin: 100% 100%;
}

@keyframes beat {
  0% {
    transform: scale(1);
  }

  50% {
    transform: scale(1.2);
  }

  100% {
    transform: scale(1);
  }
}

@keyframes coldblue {
  0%, 100% {
    background-color: rgb(255, 255, 255);
  }

  50% {
    background-color: rgb(91, 91, 241);
  }
}
</style>