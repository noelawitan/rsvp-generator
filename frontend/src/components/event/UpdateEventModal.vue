<template>
  <div class="modal fade" id="updateEventModal" tabindex="-1" ref="myModal">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">Edit Event</h5>
          <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
        </div>
        <div class="modal-body">
          <event-editor ref="updateEventEditor" :event="event" @submitted-event="handleSubmittedEvent"
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
  props: {
    eventObj: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      updateEventModal: null,
      event: null
    };
  },
  mounted() {
    this.updateEventModal = new Modal('#updateEventModal');
  },
  methods: {
    handleSubmittedEvent(updatedEvent) {
      this.$emit('submittedEvent', updatedEvent);
    },
    show() {
      this.event = JSON.parse(JSON.stringify(this.eventObj));
      this.$refs.updateEventEditor.initializeEventObj(this.event);
      this.updateEventModal.show();
    },
    hide() {
      this.updateEventModal.hide();
    }
  }
};
</script>