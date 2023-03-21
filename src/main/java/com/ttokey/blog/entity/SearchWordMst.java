package com.ttokey.blog.entity;

import com.ttokey.blog.dto.TopTenWordInfo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@Entity
@Table(name = "SEARCH_WORD_MST",
        indexes = {
                @Index(columnList = "word")
        }
)
public class SearchWordMst {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    @Column(unique = true)
    String word;
    Long count;
    @CreationTimestamp
    LocalDateTime insertDate;
    @UpdateTimestamp
    LocalDateTime updateDate;

    @Builder
    public SearchWordMst(String word) {
        this.word = word;
        this.count = 0L;
    }

    public SearchWordHst toHistory() {
        return SearchWordHst.builder().word(this.word).build();
    }

    public SearchWordTopTen toTopTen() {
        return SearchWordTopTen.builder().id(this.id).build();
    }

    public TopTenWordInfo toTopTenWordInfo() {
        return TopTenWordInfo.builder()
                .word(this.word)
                .count(this.count)
                .build();
    }

    public void addCount() {
        this.count++;
    }
}
