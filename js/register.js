document.addEventListener('DOMContentLoaded', () => {
  const form = document.getElementById('registerForm');

  form.addEventListener('submit', (event) => {
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

    const students = JSON.parse(localStorage.getItem('students') || '[]');
    const duplicate = students.find((student) => student.email === data.email || student.rollNumber === data.rollNumber);

    if (duplicate) {
      showMessage('A student with the same email or roll number already exists.', 'error');
      return;
    }

    students.push({ ...data, id: Date.now() });
    localStorage.setItem('students', JSON.stringify(students));
    localStorage.setItem('currentStudent', JSON.stringify({ email: data.email, name: data.fullName }));
    showMessage('Registration successful. You can now log in.', 'success');
    form.reset();
    setTimeout(() => window.location.href = 'login.html', 800);
  });
});
