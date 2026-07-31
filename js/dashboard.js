document.addEventListener('DOMContentLoaded', () => {
  const currentStudent = JSON.parse(localStorage.getItem('currentStudent') || 'null');
  const studentName = document.getElementById('studentName');

  if (studentName) {
    studentName.textContent = currentStudent?.name || 'Student';
  }

  const events = JSON.parse(localStorage.getItem('events') || '[]');
  const registrations = JSON.parse(localStorage.getItem('registrations') || '[]');
  const upcoming = events.filter((event) => new Date(event.date) >= new Date()).length;

  document.getElementById('totalEvents').textContent = events.length || 0;
  document.getElementById('registeredEvents').textContent = registrations.length || 0;
  document.getElementById('upcomingEvents').textContent = upcoming || 0;
});
