<template>
  <nav class="navbar bg-dark border-bottom border-body sticky-top">
    <div class="container-fluid">
      <a class="navbar-brand text-white">Rsvp Generator</a>
      <form class="d-flex" @submit.prevent.stop="logout">
        <button class="btn btn-primary" type="submit">Logout</button>
      </form>
    </div>
  </nav>
</template>

<script>
export default {
  methods: {
    logout() {
      localStorage.clear();
      this.$router.push('/login');
    }
  }
}
</script>