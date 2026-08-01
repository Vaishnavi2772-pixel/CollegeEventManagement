let allEvents = [];
let currentRegistrations = [];

function formatDate(value) {
  return new Date(value).toLocaleDateString('en-US', { year: 'numeric', month: 'short', day: 'numeric' });
}

async function loadEvents() {
  try {
    const response = await fetch('http://localhost:7000/api/events');
    const result = await response.json();
    const events = result.events || [];
    allEvents = events;
    renderEvents();
  } catch (error) {
    console.error(error);
  }
}

async function loadRegistrations() {
  const currentStudent = JSON.parse(localStorage.getItem('currentStudent') || 'null');
  if (!currentStudent) {
    currentRegistrations = [];
    renderRegistrations();
    return;
  }

  try {
    const response = await fetch(`http://localhost:7000/api/registrations?studentId=${currentStudent.studentId || 0}`);
    const result = await response.json();
    currentRegistrations = result.registrations || [];
    renderRegistrations();
  } catch (error) {
    console.error(error);
  }
}

function renderEvents() {
  const container = document.getElementById('eventsContainer');
  if (!container) return;

  const searchValue = document.getElementById('searchInput')?.value.toLowerCase() || '';
  const filterValue = document.getElementById('filterSelect')?.value || 'all';
  const sortValue = document.getElementById('sortSelect')?.value || 'date';

  let events = allEvents.filter((event) => {
    const matchesText = event.eventName.toLowerCase().includes(searchValue) || event.description.toLowerCase().includes(searchValue);
    const matchesFilter = filterValue === 'all' || event.category === filterValue;
    return matchesText && matchesFilter;
  });

  events.sort((a, b) => {
    if (sortValue === 'name') return a.eventName.localeCompare(b.eventName);
    if (sortValue === 'seats') return b.availableSeats - a.availableSeats;
    return new Date(a.eventDate) - new Date(b.eventDate);
  });

  container.innerHTML = '';

  if (events.length === 0) {
    container.innerHTML = '<p class="event-card">No events match your current filters.</p>';
    return;
  }

  const currentStudent = JSON.parse(localStorage.getItem('currentStudent') || 'null');
  events.forEach((event) => {
    const registered = currentRegistrations.some((entry) => entry.eventName === event.eventName);
    const card = document.createElement('article');
    card.className = 'event-card';
    card.innerHTML = `
      <span class="badge">${event.category}</span>
      <h3>${event.eventName}</h3>
      <p>${event.description}</p>
      <div class="event-meta">
        <p><strong>Date:</strong> ${formatDate(event.eventDate)}</p>
        <p><strong>Venue:</strong> ${event.venue}</p>
        <p><strong>Available Seats:</strong> ${event.availableSeats}</p>
      </div>
      <div class="event-actions">
        <span class="badge status-pill">${registered ? 'Registered' : 'Open'}</span>
        <button class="btn btn-small" data-event-id="${event.eventId}" ${event.availableSeats <= 0 || registered || !currentStudent ? 'disabled' : ''}>${registered ? 'Registered' : 'Register'}</button>
      </div>
    `;
    container.appendChild(card);
  });
}

async function registerForEvent(eventId) {
  const currentStudent = JSON.parse(localStorage.getItem('currentStudent') || 'null');
  if (!currentStudent) {
    window.location.href = 'login.html';
    return;
  }

  try {
    const response = await fetch('http://localhost:7000/api/events', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ studentId: currentStudent?.studentId || 0, eventId })
    });
    const result = await response.json();
    if (!response.ok || !result.success) {
      alert(result.message || 'Unable to register.');
      return;
    }
    await loadEvents();
    await loadRegistrations();
  } catch (error) {
    alert('Unable to reach the server.');
  }
}

function renderRegistrations() {
  const container = document.getElementById('registrationsContainer');
  if (!container) return;

  container.innerHTML = '';
  if (currentRegistrations.length === 0) {
    container.innerHTML = '<p class="event-card">You have no registrations yet.</p>';
    return;
  }

  currentRegistrations.forEach((registration) => {
    const card = document.createElement('article');
    card.className = 'event-card';
    card.innerHTML = `
      <h3>${registration.eventName}</h3>
      <div class="event-meta">
        <p><strong>Date:</strong> ${registration.eventDate}</p>
        <p><strong>Venue:</strong> ${registration.venue}</p>
      </div>
      <div class="event-actions">
        <button class="btn btn-secondary btn-small" data-cancel-id="${registration.registrationId}">Cancel</button>
      </div>
    `;
    container.appendChild(card);
  });
}

async function cancelRegistration(registrationId) {
  try {
    const response = await fetch('http://localhost:7000/api/registrations', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ registrationId })
    });
    const result = await response.json();
    if (!response.ok || !result.success) {
      alert(result.message || 'Unable to cancel.');
      return;
    }
    await loadRegistrations();
    await loadEvents();
  } catch (error) {
    alert('Unable to reach the server.');
  }
}

document.addEventListener('DOMContentLoaded', () => {
  loadEvents();
  loadRegistrations();

  document.getElementById('searchInput')?.addEventListener('input', renderEvents);
  document.getElementById('filterSelect')?.addEventListener('change', renderEvents);
  document.getElementById('sortSelect')?.addEventListener('change', renderEvents);

  document.addEventListener('click', (event) => {
    const registerButton = event.target.closest('[data-event-id]');
    if (registerButton) {
      registerForEvent(registerButton.getAttribute('data-event-id'));
    }

    const cancelButton = event.target.closest('[data-cancel-id]');
    if (cancelButton) {
      cancelRegistration(cancelButton.getAttribute('data-cancel-id'));
    }
  });
});
