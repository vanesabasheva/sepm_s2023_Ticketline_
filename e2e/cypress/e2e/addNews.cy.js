context('add news', () => {
    let msgText = 'news' + new Date().getTime();

    it('create news', () => {
        cy.loginAdmin();
        //cy.createNews(msgText); no longer possible with image upload & dynamic event search
    })

});
