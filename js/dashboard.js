document.addEventListener('DOMContentLoaded', async () => {
  const currentStudent = JSON.parse(localStorage.getItem('currentStudent') || 'null');
  const studentName = document.getElementById('studentName');

  if (studentName) {
    studentName.textContent = currentStudent?.name || 'Student';
  }

  try {
    const response = await fetch('http://localhost:7000/api/events');
    const result = await response.json();
    const events = result.events || [];
    const upcoming = events.filter((event) => new Date(event.eventDate) >= new Date()).length;
    document.getElementById('totalEvents').textContent = events.length || 0;
    document.getElementById('upcomingEvents').textContent = upcoming || 0;
  } catch (error) {
    document.getElementById('totalEvents').textContent = '0';
    document.getElementById('upcomingEvents').textContent = '0';
  }

  try {
    const response = await fetch(`http://localhost:7000/api/registrations?studentId=${currentStudent?.studentId || 0}`);
    const result = await response.json();
    document.getElementById('registeredEvents').textContent = result.registrations?.length || 0;
  } catch (error) {
    document.getElementById('registeredEvents').textContent = '0';
  }
});
