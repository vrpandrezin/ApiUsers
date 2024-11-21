# Endpoints da aplicação.

### Endpoints:

- [**GET**]() (/users/userList)
- [**POST**]() (/users/addUser)
- [**PUT**](#put) (/users/updateUser/{id})
- [**DELETE**]() (/users/remUser/{id})

---

### PUT

Esse endpoint foi desenvolvido com o intuito de atualizar os dados do usuário sempre que necessário.

- Forma correta:

  Por padrão, é necessário que o endpoint contenha dados de autorização (*que estejam cadastrados no Banco de Dados*) para realizar a alteração dos dados do usuário.

  ![Autorização correta](documentacao-API/autorizacao-correta.jpg)

  Após preencher a autorização, insira os dados que serão atualizados no corpo da requisição. Essa alteração pode ser completa ou parcial.

  ![Payload correto](documentacao-API/payload-correto.jpg)
