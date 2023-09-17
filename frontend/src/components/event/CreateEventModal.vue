<template>
  <div class="modal fade" id="createEventModal" tabindex="-1" ref="myModal">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">Create Event</h5>
          <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
        </div>
        <div class="modal-body">
          <event-editor ref="createEventEditor" :event="event" @submitted-event="handleSubmittedEvent"
                        @cancel="hide"/>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Modal from "bootstrap/js/dist/modal";
import EventEditor from "@/components/event/EventEditor.vue";

export default {
  components: {EventEditor},
  emits: ['submittedEvent'],
  data() {
    return {
      createEventModal: null,
      event: {
        name: '',
        location: '',
        date: '',
        startTime: '',
        endTime: '',
        invitationResponseRedirectUrl: ''
      },
    };
  },
  mounted() {
    this.createEventModal = new Modal('#createEventModal');
  },
  methods: {
    clearData() {
      this.event = {
        name: '',
        location: '',
        date: '',
        startTime: '',
        endTime: '',
        invitationResponseRedirectUrl: ''
      };
    },
    handleSubmittedEvent(newEvent) {
      this.$emit('submittedEvent', newEvent);
    },
    show() {
      this.clearData();
      this.$refs.createEventEditor.initializeEventObj(this.event);
      this.createEventModal.show();
    },
    hide() {
      this.createEventModal.hide();
    }
  }
}
</script>