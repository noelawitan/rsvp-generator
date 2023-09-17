<template>
  <div class="login-container">
    <div class="div-center">
      <div class="content">
        <h3>Rsvp Login</h3>
        <hr/>
        <form @submit.prevent.stop="login">
          <div v-if="errorLogin" class="text-danger">{{ errorLogin }}</div>
          <div class="form-floating mb-3">
            <input type="email" class="form-control" id="emailInput" placeholder="your-email@example.com"
                   v-model="email" required>
            <label for="emailInput">Email address</label>
          </div>
          <div class="form-floating">
            <input type="password" class="form-control" id="passwordInput" placeholder="Password" v-model="password"
                   required>
            <label for="passwordInput">Password</label>
          </div>
          <div class="form-group text-center mt-2">
            <button type="submit" class="btn btn-primary">Login</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import {LOGIN_URL} from '@/js/config.js';

export default {
  data() {
    return {
      email: '',
      password: '',
      errorLogin: ''
    }
  },
  methods: {
    async login() {
      const requestBody = {
        email: this.email,
        password: this.password
      };

      this.$showLoader;
      const response = await fetch(LOGIN_URL, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(requestBody)
      });

      if (response.ok) {
        const data = await response.json();

        localStorage.setItem('access_token', data.access_token);
        localStorage.setItem('refresh_token', data.refresh_token);

        this.$router.push('/dashboard');
      } else {
        this.errorLogin = "Incorrect credentials";
      }
    }
  }

}
</script>

<style scoped>
label {
  font-weight: normal;
}

.login-container {
  width: 100%;
  position: absolute;
  top: 0;
  bottom: 0;
}

.div-center {
  width: 400px;
  height: 300px;
  background-color: #fff;
  position: absolute;
  left: 0;
  right: 0;
  top: 0;
  bottom: 0;
  margin: auto;
  max-width: 100%;
  max-height: 100%;
  overflow: auto;
  padding: 1em 2em;
  border-bottom: 2px solid #ccc;
  border-radius: 10px;
  display: table;
}

div.content {
  display: table-cell;
  vertical-align: middle;

}
</style>