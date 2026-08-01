document.addEventListener('DOMContentLoaded', () => {
  const loginSection = document.getElementById('adminLoginSection');
  const dashboard = document.getElementById('adminDashboard');
  const loginForm = document.getElementById('adminLoginForm');
  const eventForm = document.getElementById('eventForm');
  const logoutBtn = document.getElementById('logoutAdminBtn');
  const eventsList = document.getElementById('adminEventsList');
  const registrationsList = document.getElementById('adminRegistrationsList');

  if (localStorage.getItem('adminLoggedIn') === 'true') {
    loginSection.hidden = true;
    dashboard.hidden = false;
    loadAdminData();
  }

  loginForm?.addEventListener('submit', async (event) => {
    event.preventDefault();
    const email = document.getElementById('adminEmail').value.trim();
    const password = document.getElementById('adminPassword').value.trim();

    try {
      const response = await fetch('http://localhost:7000/api/admin/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, password })
      });
      const result = await response.json();
      if (!response.ok || !result.success) {
        alert(result.message || 'Invalid admin credentials.');
        return;
      }

      localStorage.setItem('adminLoggedIn', 'true');
      loginSection.hidden = true;
      dashboard.hidden = false;
      loadAdminData();
    } catch (error) {
      alert('Unable to connect to the server.');
    }
  });

  logoutBtn?.addEventListener('click', () => {
    localStorage.removeItem('adminLoggedIn');
    loginSection.hidden = false;
    dashboard.hidden = true;
  });

  eventForm?.addEventListener('submit', async (event) => {
    event.preventDefault();
    const payload = {
      eventId: document.getElementById('eventId').value || 0,
      eventName: document.getElementById('eventName').value.trim(),
      description: document.getElementById('description').value.trim(),
      eventDate: document.getElementById('eventDate').value,
      venue: document.getElementById('venue').value.trim(),
      availableSeats: document.getElementById('availableSeats').value,
      category: document.getElementById('category').value.trim()
    };

    try {
      const response = payload.eventId ? await fetch('http://localhost:7000/api/admin/events', { method: 'PUT', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(payload) }) : await fetch('http://localhost:7000/api/admin/events', { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(payload) });
      const result = await response.json();
      if (!response.ok || !result.success) {
        alert(result.message || 'Unable to save event.');
        return;
      }
      eventForm.reset();
      loadAdminData();
    } catch (error) {
      alert('Unable to connect to the server.');
    }
  });

  async function loadAdminData() {
    try {
      const [eventsResponse, registrationsResponse] = await Promise.all([
        fetch('http://localhost:7000/api/admin/events'),
        fetch('http://localhost:7000/api/admin/registrations')
      ]);
      const eventsResult = await eventsResponse.json();
      const registrationsResult = await registrationsResponse.json();
      renderEvents(eventsResult.events || []);
      renderRegistrations(registrationsResult.registrations || []);
    } catch (error) {
      eventsList.innerHTML = '<p>Unable to load data.</p>';
      registrationsList.innerHTML = '<p>Unable to load data.</p>';
    }
  }

  function renderEvents(events) {
    if (!events.length) {
      eventsList.innerHTML = '<p>No events found.</p>';
      return;
    }

    const rows = events.map((event) => `
      <tr>
        <td>${event.eventName}</td>
        <td>${event.category}</td>
        <td>${event.availableSeats}</td>
        <td>
          <div class="inline-actions">
            <button class="btn btn-small" data-edit-id="${event.eventId}">Edit</button>
            <button class="btn btn-secondary btn-small" data-delete-id="${event.eventId}">Delete</button>
          </div>
        </td>
      </tr>
    `).join('');

    eventsList.innerHTML = `<table><thead><tr><th>Name</th><th>Category</th><th>Seats</th><th>Actions</th></tr></thead><tbody>${rows}</tbody></table>`;
  }

  function renderRegistrations(registrations) {
    if (!registrations.length) {
      registrationsList.innerHTML = '<p>No registrations found.</p>';
      return;
    }

    const rows = registrations.map((registration) => `<tr><td>${registration}</td></tr>`).join('');
    registrationsList.innerHTML = `<table><tbody>${rows}</tbody></table>`;
  }

  eventsList?.addEventListener('click', async (event) => {
    const editButton = event.target.closest('[data-edit-id]');
    if (editButton) {
      const eventId = Number(editButton.getAttribute('data-edit-id'));
      const response = await fetch('http://localhost:7000/api/admin/events');
      const result = await response.json();
      const target = (result.events || []).find((entry) => entry.eventId === eventId);
      if (target) {
        document.getElementById('eventId').value = target.eventId;
        document.getElementById('eventName').value = target.eventName;
        document.getElementById('description').value = target.description;
        document.getElementById('eventDate').value = target.eventDate;
        document.getElementById('venue').value = target.venue;
        document.getElementById('availableSeats').value = target.availableSeats;
        document.getElementById('category').value = target.category;
      }
      return;
    }

    const deleteButton = event.target.closest('[data-delete-id]');
    if (deleteButton) {
      const eventId = Number(deleteButton.getAttribute('data-delete-id'));
      const response = await fetch(`http://localhost:7000/api/admin/events?eventId=${eventId}`, { method: 'DELETE' });
      const result = await response.json();
      if (!response.ok || !result.success) {
        alert(result.message || 'Unable to delete event.');
        return;
      }
      loadAdminData();
    }
  });
});
