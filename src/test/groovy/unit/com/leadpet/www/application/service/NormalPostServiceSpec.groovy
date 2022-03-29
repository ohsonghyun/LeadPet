package com.leadpet.www.application.service

import com.leadpet.www.infrastructure.db.NormalPostsRepository
import com.leadpet.www.infrastructure.db.UsersRepository
import spock.lang.Ignore
import spock.lang.Specification

/**
 * NormalPostServiceSpec
 */
@Ignore
class NormalPostServiceSpec extends Specification {

    private NormalPostService normalPostService
    private NormalPostsRepository normalPostsRepository
    private UsersRepository usersRepository

    def setup() {
        this.usersRepository = Mock(UsersRepository.class)
        this.normalPostsRepository = Mock(NormalPostsRepository.class)
        this.normalPostService = new NormalPostService(usersRepository, normalPostsRepository)
    }

}
