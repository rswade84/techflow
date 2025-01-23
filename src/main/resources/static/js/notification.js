const socket = new SockJS('/ws');
    const stompClient = Stomp.over(socket);
    const pathname = window.location.pathname;
    let ticketId;
    const notificationDiv = document.getElementById('notification');
    const messageElement = document.createElement('div');
    const closeButton = document.getElementById('notification-close');

    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
            stompClient.subscribe('/topic/notifications', function (notification) {
                ticketId = notification.body.split("ID ")[1];
                if (!pathname.includes("tickets/edit/" + ticketId)) {
                    displayNotification(notification.body);
                }
            });
    });

    closeButton.addEventListener("click", close);

function displayNotification(notification) {
        messageElement.textContent = notification;
        notificationDiv.insertBefore(messageElement, notificationDiv.firstChild);
        notificationDiv.classList.remove('hidden');
        messageElement.classList.add('inline');
        notificationDiv.classList.add('inline');
    }

    function close() {
        notificationDiv.classList.add('hidden');
        messageElement.remove();

    }