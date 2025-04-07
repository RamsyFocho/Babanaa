  const loginBtn = document.getElementById('editBtn');
    const loginModal = document.getElementById('loginModal');
    const closeModal = document.getElementById('closeModal');

    loginBtn.addEventListener('click', () => {
        loginModal.classList.add('active');
    });

    closeModal.addEventListener('click', () => {
        loginModal.classList.remove('active');
    });
