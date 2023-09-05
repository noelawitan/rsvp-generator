<template>
  <table class="table table-hover">
    <thead>
    <tr>
      <th>First Name</th>
      <th>Last Name</th>
      <th>Email</th>
      <th>Action</th>
    </tr>
    </thead>
    <tbody>
    <tr v-for="invitation in invitations" :key="invitation.id">
      <td>{{ invitation.guest.firstName }}</td>
      <td>{{ invitation.guest.lastName }}</td>
      <td>{{ invitation.guest.email }}</td>
      <td>
        <button class="btn btn-primary">Edit</button>
        <button class="btn btn-danger">Remove</button>
      </td>
    </tr>
    </tbody>
  </table>
  <!--TODO: Make this mobile responsive, by hiding the table and showing an element with collapsable arrow -->
</template>

<script>
import {INVITATION_URL} from "@/config/config";

export default {
  props: {
    eventId: {
      type: Number,
      required: true
    }
  },
  data() {
    return {
      invitations: null
    };
  },
  created() {
    this.getAllUserInvitation();
  },
  methods: {
    getAllUserInvitation() {
      const accessToken = localStorage.getItem('access_token');

      if (accessToken !== null) {
        fetch(`${INVITATION_URL}/event/${this.eventId}`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${accessToken}`
          }
        })
            .then(response => {
              if (response.status === 403) {
                localStorage.clear();
                this.$router.push('/login');
              }

              return response.json();
            })
            .then(data => {
              if (Array.isArray(data)) {
                this.invitations = data;
              }
            })
            .catch((error) => {
              // TODO: Create a modal that says there's something wrong in the server
              console.error('Error:', error);
            });
      } else {
        this.$router.push('/login');
      }
    }
  }
};
</script>