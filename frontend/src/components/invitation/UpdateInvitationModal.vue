<template>
  <div class="modal fade" id="updateInvitationModal" tabindex="-1" ref="myModal">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">Edit Invitation</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
        </div>
        <div class="modal-body">
          <invitation-editor ref="updateInvitationEditor" :invitation="invitationObj"
                             @submitted-invitation="handleSubmittedInvitation"
                             @cancel="hide"/>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Modal from "bootstrap/js/dist/modal";
import InvitationEditor from "@/components/invitation/InvitationEditor.vue";
import {INVITATION_URL} from "@/js/config";

export default {
  components: {InvitationEditor},
  emits: ['submittedInvitation'],
  data() {
    return {
      updateInvitationModal: null,
      invitationObj: null
    };
  },
  mounted() {
    this.updateInvitationModal = new Modal('#updateInvitationModal');
  },
  methods: {
    handleSubmittedInvitation(updatedInvitation) {
      // Set the deadline to null if they turned off the sendInvitation.
      if (!updatedInvitation.sendInvitation) {
        updatedInvitation.deadLine = null;
      }
      this.$emit('submittedInvitation', updatedInvitation);
    },
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
        })
            .then(response => {
              if (response.status === 403) {
                this.$router.push('/login');
                return;
              }

              return response.json();
            })
            .then(data => {
              if (data !== null) {
                if ('deadLine' in data) {
                  if (data.deadLine !== null) {
                    data['sendInvitation'] = true;
                  }
                }
                this.$refs.updateInvitationEditor.initializeInvitationObj(data);
                this.updateInvitationModal.show();
              }
            })
            .catch((error) => {
              // TODO: Create a modal that says there's something wrong in the server
              console.error('Error:', error);
            })
            .finally(() => {
              this.$loader.isVisible = false;
            });
      } else {
        this.$router.push('/login');
      }
    },
    hide() {
      this.updateInvitationModal.hide();
    }
  }
};
</script>