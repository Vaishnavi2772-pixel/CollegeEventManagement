document.addEventListener('DOMContentLoaded', () => {
  const form = document.getElementById('registerForm');
  if (!form) return;

  form.addEventListener('submit', async (event) => {
    event.preventDefault();
    clearMessages();

    const data = {
      fullName: document.getElementById('fullName').value.trim(),
      rollNumber: document.getElementById('rollNumber').value.trim(),
      department: document.getElementById('department').value.trim(),
      year: document.getElementById('year').value.trim(),
      email: document.getElementById('email').value.trim(),
      phone: document.getElementById('phone').value.trim(),
      password: document.getElementById('password').value.trim()
    };

    if (!data.fullName || !data.rollNumber || !data.department || !data.year || !data.email || !data.phone || !data.password) {
      showMessage('All fields are required.', 'error');
      return;
    }

    if (!validateEmail(data.email)) {
      showMessage('Please enter a valid email address.', 'error');
      return;
    }

    if (!validatePhone(data.phone)) {
      showMessage('Phone number must be exactly 10 digits.', 'error');
      return;
    }

    if (!validatePassword(data.password)) {
      showMessage('Password must be at least 8 characters long.', 'error');
      return;
    }

    try {
      const response = await fetch('http://localhost:8080/api/register', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
      });
      const result = await response.json();
      if (!response.ok || !result.success) {
        showMessage(result.message || 'Registration failed.', 'error');
        return;
      }

      localStorage.setItem('currentStudent', JSON.stringify({ studentId: result.studentId, email: data.email, name: data.fullName }));
      showMessage(result.message || 'Registration successful. You can now log in.', 'success');
      form.reset();
      setTimeout(() => window.location.href = 'login.html', 800);
    } catch (error) {
      showMessage('Unable to reach the server. Please ensure the Java app is running.', 'error');
    }
  });
});
