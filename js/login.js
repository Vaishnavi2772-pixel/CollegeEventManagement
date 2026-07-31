document.addEventListener('DOMContentLoaded', () => {
  const form = document.getElementById('loginForm');

  form.addEventListener('submit', (event) => {
    event.preventDefault();
    clearMessages();

    const email = document.getElementById('email').value.trim();
    const password = document.getElementById('password').value.trim();

    if (!email || !password) {
      showMessage('Email and password are required.', 'error');
      return;
    }

    const students = JSON.parse(localStorage.getItem('students') || '[]');
    const student = students.find((entry) => entry.email === email && entry.password === password);

    if (!student) {
      showMessage('Invalid credentials. Please try again.', 'error');
      return;
    }

    localStorage.setItem('currentStudent', JSON.stringify({ email: student.email, name: student.fullName }));
    window.location.href = 'dashboard.html';
  });
});
