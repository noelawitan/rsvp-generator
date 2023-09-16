export function formatDate(date) {
    if (date !== null) {
        let options = {year: 'numeric', month: 'long', day: 'numeric'};
        return new Date(date).toLocaleDateString(undefined, options);
    }

    return '--';
}

export function formatTime(time) {
    if (!time) return 'N/A';
    let options = {hour: '2-digit', minute: '2-digit', hour12: true};
    return new Date(`1970-01-01T${time}`).toLocaleTimeString(undefined, options);
}

export function formatDateTime(dateTime) {
    if (dateTime !== null) {
        let formattedDate = formatDate(dateTime);
        let formattedTime = formatTime(dateTime.split('T')[1]);

        return `${formattedDate} at ${formattedTime}`;
    }

    return '--';
}