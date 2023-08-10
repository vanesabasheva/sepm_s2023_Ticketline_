declare namespace Cypress{
    interface Chainable {
        /**
         * Navigate to main page and login as admin
         */
        loginAdmin();

        /**
         * Creates a news with a given text
         * @param msg the text of the created news
         */
        createNews(msg: string);
    }
}
