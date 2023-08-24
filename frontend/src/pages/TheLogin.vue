<template>
  <div class="login-container">
    <div class="div-center">
      <div class="content">
        <h3>Login</h3>
        <hr/>
        <form @submit.prevent.stop="login">
          <div class="form-group">
            <label for="email">Email address</label>
            <input type="email" class="form-control" id="email" placeholder="Email" v-model="email">
          </div>
          <div class="form-group">
            <label for="password">Password</label>
            <input type="password" class="form-control" id="password" placeholder="Password" v-model="password">
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
import {LOGIN_URL} from '@/config/config.js';

export default {
  data() {
    return {
      email: '',
      password: ''
    }
  },
  methods: {
    async login() {
      const requestBody = {
        email: this.email,
        password: this.password
      };

      const response = await fetch(LOGIN_URL, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(requestBody)
      });

      if (response.ok) {
        const data = await response.json();

        localStorage.setItem('accessToken', data.access_token);
        localStorage.setItem('refreshToken', data.refresh_token);

        this.$router.push('/dashboard');
      } else {
        console.error('Authentication failed');
      }
    }
  }

}
</script>
<style scoped>
.login-container {
  background: #e2e2e2;
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