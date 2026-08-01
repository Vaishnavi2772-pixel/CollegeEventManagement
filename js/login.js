document.addEventListener('DOMContentLoaded', () => {
  const form = document.getElementById('loginForm');
  if (!form) return;

  form.addEventListener('submit', async (event) => {
    event.preventDefault();
    clearMessages();

    const email = document.getElementById('email').value.trim();
    const password = document.getElementById('password').value.trim();

    if (!email || !password) {
      showMessage('Email and password are required.', 'error');
      return;
    }

    try {
      const response = await fetch('http://localhost:7000/api/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, password })
      });
      const result = await response.json();
      if (!response.ok || !result.success) {
        showMessage(result.message || 'Invalid credentials. Please try again.', 'error');
        return;
      }

      localStorage.setItem('currentStudent', JSON.stringify({ studentId: result.student.studentId, email: result.student.email, name: result.student.name }));
      window.location.href = 'dashboard.html';
    } catch (error) {
      showMessage('Unable to reach the server. Please ensure the Java app is running.', 'error');
    }
  });
});
