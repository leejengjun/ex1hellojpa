package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {

    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try{

        /* 회원 등록
            Member member = new Member();
            member.setId(2L);
            member.setName("HelloB");

            em.persist(member);
        */

        /* 회원 검색
            Member findMember = em.find(Member.class, 1L);
            System.out.println("findMember.id = " + findMember.getId());
            System.out.println("findMember.name = " + findMember.getName());
        */

        /* 회원 삭제
            Member findMember = em.find(Member.class, 1L);

            em.remove(findMember);

         */
        /*     회원 수정 : JPA가 관리하며 트랜젝셔널 커밋 시점에서 체크해서 변경된 부분이 있으면 업데이트 쿼리를 만들어 날린다.
            Member findMember = em.find(Member.class, 1L);
            findMember.setName("HelloJPA");
        */
        /*  객체 Member에 있는 모든 데이터 조회
            List<Member> result = em.createQuery("select m from Member as m", Member.class)
                    .getResultList();   // 쿼리 대상이 table이 아니라 '객체'를 대상으로 함. 즉 m으로 지칭하는 Member의 모든 객체를 불러옴

            for (Member member : result) {
                System.out.println("member.name = " + member.getName());
            }

            실행된 쿼리문과 결과
            select
                member0_.id as id1_0_,
                member0_.name as name2_0_
            from
                Member member0_
            member.name = HelloJPA
            member.name = HelloB
        */
        /*  페이징 처리
            List<Member> result = em.createQuery("select m from Member as m", Member.class)
                    .setFirstResult(5)
                    .setMaxResults(8)
                    .getResultList();

            실행쿼리문
            select
                member0_.id as id1_0_,
                member0_.name as name2_0_
            from
                Member member0_ limit ? offset ?
         */
/*

            // 비영속
            Member member = new Member();
            member.setId(101L);
            member.setName("HelloJPA");
*/

        /*
            //영속
            System.out.println("=== BEFORE ===");
            em.persist(member);
            System.out.println("=== AFTER ===");

             //영속 부분을 실행하면 결과가
             === BEFORE ===
             === AFTER ===
             즉, 비포 에프더 안에 아무런 처리가 없다.
        */

        /*
            //영속
            System.out.println("=== BEFORE ===");
            em.persist(member); // 여기서 1차 캐시에 저장 되었다.
            System.out.println("=== AFTER ===");

            Member findMember = em.find(Member.class, 101L);    // 여기서 select 쿼리문이 실행되지 않고 캐시에 저장된 값을 읽어서 뿌려준다.

            System.out.println("findMember.id = " + findMember.getId());
            System.out.println("findMember.name = " + findMember.getName());

            //콘솔 실행화면
            === BEFORE ===
            === AFTER ===
            findMember.id = 101
            findMember.name = HelloJPA
            Hibernate:
                /* insert hellojpa.Member
                    *//* insert
                                into
                        Member
                                (name, id)
                        values
                                (?, ?)
        */
        /*
            //영속
            Member findMember1 = em.find(Member.class, 101L);   // select 쿼리가 실행하여 영속 컨텍스트에 올림
            Member findMember2 = em.find(Member.class, 101L);   // select 쿼리를 실행전 먼저 영속 컨텍스트에 있는 1차 캐시를 확인함.

            //콘솔 실행화면
            Hibernate:
            select
                member0_.id as id1_0_0_,
                member0_.name as name2_0_0_
            from
                Member member0_
            where
                member0_.id=?
        */
        /*

            //영속
            Member findMember1 = em.find(Member.class, 101L);
            Member findMember2 = em.find(Member.class, 101L);

            System.out.println("result = " + (findMember1 == findMember2));
            // JPA가 영속 엔티티의 동일성을 보장함. 1차 캐시가 있어서 가능함.
            //콘솔 실행화면
            Hibernate:
                select
                    member0_.id as id1_0_0_,
                    member0_.name as name2_0_0_
                from
                    Member member0_
                where
                    member0_.id=?
            result = true
        */
        /*

            //영속
            Member member1 = new Member(150L, "A");
            Member member2 = new Member(160L, "B");

            em.persist(member1);
            em.persist(member2);    // 영속 컨텍스트에 member1과 member2의 쿼리문(SQL: INSERT)이 차곡차곡 쌓임

            System.out.println("============================");

            //콘솔 실행화면

        */
/*

            //영속
            Member member = em.find(Member.class, 150L);
            member.setName("ZZZZZ");
        //    em.persist(member);   // JPA는 찾아와서 값을 변경하고 다시 넣을 필요가 없다. 즉, 해당 코드가 필요x

            System.out.println("============================");
            //콘솔 실행화면
            Hibernate:
            select
                member0_.id as id1_0_0_,
                member0_.name as name2_0_0_
            from
                Member member0_
            where
                member0_.id=?
        ============================
        Hibernate:
            /* update
                hellojpa.Member */ /* update
                            Member
                    set
                    name=?
                    where
                    id=?

         //   이후 DB(H2)가서 id 150을 조회하면 NAME이 ZZZZZ로 바뀐 것을 확인할 수 있다.

*/

            tx.commit();    // 커밋 되는 순간에 쿼리가 날라감!
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
