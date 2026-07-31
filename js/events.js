const defaultEvents = [
  {
    id: 1,
    name: 'Hackathon 2026',
    description: 'A 24-hour coding challenge for innovative solutions.',
    date: '2026-08-15',
    venue: 'Main Auditorium',
    seats: 80,
    category: 'Technical'
  },
  {
    id: 2,
    name: 'Cultural Fest',
    description: 'Music, dance, art and cultural showcases.',
    date: '2026-09-10',
    venue: 'Open Air Theatre',
    seats: 120,
    category: 'Cultural'
  },
  {
    id: 3,
    name: 'Science Expo',
    description: 'Student projects and research demonstrations.',
    date: '2026-10-05',
    venue: 'Science Block',
    seats: 60,
    category: 'Academic'
  },
  {
    id: 4,
    name: 'Sports Meet',
    description: 'Inter-departmental competitions and fun events.',
    date: '2026-11-12',
    venue: 'Sports Ground',
    seats: 150,
    category: 'Sports'
  }
];

function seedEvents() {
  if (!localStorage.getItem('events')) {
    localStorage.setItem('events', JSON.stringify(defaultEvents));
  }
}

function getEvents() {
  return JSON.parse(localStorage.getItem('events') || '[]');
}

function getRegistrations() {
  return JSON.parse(localStorage.getItem('registrations') || '[]');
}

function saveEvents(events) {
  localStorage.setItem('events', JSON.stringify(events));
}

function saveRegistrations(registrations) {
  localStorage.setItem('registrations', JSON.stringify(registrations));
}

function formatDate(value) {
  return new Date(value).toLocaleDateString('en-US', { year: 'numeric', month: 'short', day: 'numeric' });
}

function renderEvents() {
  const container = document.getElementById('eventsContainer');
  if (!container) return;

  const searchValue = document.getElementById('searchInput')?.value.toLowerCase() || '';
  const filterValue = document.getElementById('filterSelect')?.value || 'all';
  const sortValue = document.getElementById('sortSelect')?.value || 'date';

  let events = getEvents().filter((event) => {
    const matchesText = event.name.toLowerCase().includes(searchValue) || event.description.toLowerCase().includes(searchValue);
    const matchesFilter = filterValue === 'all' || event.category === filterValue;
    return matchesText && matchesFilter;
  });

  events.sort((a, b) => {
    if (sortValue === 'name') return a.name.localeCompare(b.name);
    if (sortValue === 'seats') return b.seats - a.seats;
    return new Date(a.date) - new Date(b.date);
  });

  container.innerHTML = '';

  if (events.length === 0) {
    container.innerHTML = '<p class="event-card">No events match your current filters.</p>';
    return;
  }

  events.forEach((event) => {
    const card = document.createElement('article');
    card.className = 'event-card';
    const registrations = getRegistrations();
    const registered = registrations.some((entry) => entry.eventId === event.id);

    card.innerHTML = `
      <span class="badge">${event.category}</span>
      <h3>${event.name}</h3>
      <p>${event.description}</p>
      <div class="event-meta">
        <p><strong>Date:</strong> ${formatDate(event.date)}</p>
        <p><strong>Venue:</strong> ${event.venue}</p>
        <p><strong>Available Seats:</strong> ${event.seats}</p>
      </div>
      <div class="event-actions">
        <span class="badge status-pill">${registered ? 'Registered' : 'Open'}</span>
        <button class="btn btn-small" data-event-id="${event.id}" ${event.seats <= 0 || registered ? 'disabled' : ''}>${registered ? 'Registered' : 'Register'}</button>
      </div>
    `;

    container.appendChild(card);
  });
}

function registerForEvent(eventId) {
  const currentStudent = JSON.parse(localStorage.getItem('currentStudent') || 'null');
  if (!currentStudent) {
    window.location.href = 'login.html';
    return;
  }

  let events = getEvents();
  let registrations = getRegistrations();
  const event = events.find((entry) => entry.id === Number(eventId));
  const alreadyRegistered = registrations.some((entry) => entry.eventId === Number(eventId));

  if (!event || alreadyRegistered) return;
  if (event.seats <= 0) return;

  event.seats -= 1;
  registrations.push({ id: Date.now(), eventId: Number(eventId), studentEmail: currentStudent.email, studentName: currentStudent.name });
  saveEvents(events);
  saveRegistrations(registrations);
  renderEvents();
}

function renderRegistrations() {
  const container = document.getElementById('registrationsContainer');
  if (!container) return;

  const currentStudent = JSON.parse(localStorage.getItem('currentStudent') || 'null');
  const registrations = getRegistrations().filter((entry) => entry.studentEmail === currentStudent?.email);
  const events = getEvents();

  container.innerHTML = '';
  if (registrations.length === 0) {
    container.innerHTML = '<p class="event-card">You have no registrations yet.</p>';
    return;
  }

  registrations.forEach((registration) => {
    const event = events.find((entry) => entry.id === registration.eventId);
    const card = document.createElement('article');
    card.className = 'event-card';
    card.innerHTML = `
      <h3>${event?.name || 'Event'}</h3>
      <p>${event?.description || 'No description available.'}</p>
      <div class="event-meta">
        <p><strong>Date:</strong> ${event ? formatDate(event.date) : 'N/A'}</p>
        <p><strong>Venue:</strong> ${event?.venue || 'N/A'}</p>
      </div>
      <div class="event-actions">
        <button class="btn btn-secondary btn-small" data-cancel-id="${registration.id}">Cancel</button>
      </div>
    `;
    container.appendChild(card);
  });
}

function cancelRegistration(registrationId) {
  const registrations = getRegistrations().filter((entry) => entry.id !== Number(registrationId));
  const removed = getRegistrations().find((entry) => entry.id === Number(registrationId));
  if (!removed) return;

  const events = getEvents();
  const event = events.find((entry) => entry.id === removed.eventId);
  if (event) {
    event.seats += 1;
  }

  saveEvents(events);
  saveRegistrations(registrations);
  renderRegistrations();
  if (document.getElementById('eventsContainer')) {
    renderEvents();
  }
}

document.addEventListener('DOMContentLoaded', () => {
  seedEvents();
  renderEvents();
  renderRegistrations();

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
