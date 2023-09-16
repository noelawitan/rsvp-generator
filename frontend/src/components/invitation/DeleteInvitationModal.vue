<template>
  <div class="modal fade" id="deleteInvitationModal" tabindex="-1" ref="myModal">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">Delete Invitation Confirmation</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
        </div>
        <div class="modal-body">
          <div v-if="invitationObj" class="alert alert-danger">
            <span>Are you sure you want to remove
              <strong>
                {{ this.invitationObj.guest.firstName }}
                {{ this.invitationObj.guest.lastName }}
              </strong>
              ?
            </span>
          </div>
        </div>
        <div v-if="invitationObj" class="modal-footer">
          <button class="btn btn-danger" @click="$emit('confirmDelete', this.invitationObj.id)">Confirm</button>
          <button class="btn btn-secondary" @click="hide">Cancel</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {INVITATION_URL} from "@/js/config";
import Modal from "bootstrap/js/dist/modal";

export default {
  emits: ['confirmDelete'],
  data() {
    return {
      deleteInvitationModal: null,
      invitationObj: null
    }
  },
  mounted() {
    this.deleteInvitationModal = new Modal('#deleteInvitationModal');
  },
  methods: {
    show(invitationId) {
      const accessToken = localStorage.getItem('access_token');
      this.$loader.isVisible = true;

      if (accessToken !== null) {
        fetch(`${INVITATION_URL}/${invitationId}`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${accessToken}`
          }
        }).then(response => {
          if (response.status === 403) {
            this.$router.push('/login');
            return;
          }

          return response.json();
        }).then(data => {
          this.invitationObj = data;
          this.deleteInvitationModal.show();
        }).catch((error) => {
          // TODO: Create a modal that says there's something wrong in the server
          console.error('Error:', error);
        }).finally(() => {
          this.$loader.isVisible = false;
        });
      } else {
        this.$router.push('/login');
      }
    },
    hide() {
      this.deleteInvitationModal.hide();
    }
  }
}
</script>