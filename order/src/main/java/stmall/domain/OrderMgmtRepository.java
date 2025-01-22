package stmall.domain;

import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import stmall.domain.*;

//<<< PoEAA / Repository
@RepositoryRestResource(
    collectionResourceRel = "orderMgmts",
    path = "orderMgmts"
)
public interface OrderMgmtRepository
    extends PagingAndSortingRepository<OrderMgmt, Long> {}
